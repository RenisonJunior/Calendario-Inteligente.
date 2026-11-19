package br.com.prioriza.controller;

import br.com.prioriza.model.Tarefa;
import br.com.prioriza.repository.TarefaRepository;
import br.com.prioriza.service.RotinaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Controller
public class PaginaController {
    private final TarefaRepository tarefas;
    private final RotinaService rotina;

    public PaginaController(TarefaRepository tarefas, RotinaService rotina) {
        this.tarefas = tarefas; this.rotina = rotina;
    }

    @GetMapping("/")
    public String inicio() { return "inicio"; }

    @GetMapping("/painel")
    public String painel(Model model) {
        model.addAttribute("linhas", rotina.painel());
        model.addAttribute("minutosDisponiveis", rotina.perfil().getMinutosDisponiveis());
        return "painel";
    }

    @GetMapping("/calendario")
    public String calendario(@RequestParam(required = false) Integer ano,
                             @RequestParam(required = false) Integer mes, Model model) {
        YearMonth atual = YearMonth.now(java.time.ZoneId.of("America/Manaus"));
        YearMonth periodo;
        try {
            periodo = (ano != null && mes != null) ? YearMonth.of(ano, mes) : atual;
        } catch (DateTimeException e) {
            periodo = atual;
        }
        // Preenche as semanas de segunda a domingo, mantendo as tarefas dentro de cada dia.
        LocalDate inicio = periodo.atDay(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate fim = periodo.atEndOfMonth().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        Map<LocalDate, List<Tarefa>> porDia = new HashMap<>();
        for (Tarefa tarefa : tarefas.findAllByOrderByPrazoAsc()) {
            porDia.computeIfAbsent(tarefa.getPrazo(), d -> new ArrayList<>()).add(tarefa);
        }
        List<List<Dia>> semanas = new ArrayList<>();
        for (LocalDate data = inicio; !data.isAfter(fim); ) {
            List<Dia> semana = new ArrayList<>();
            for (int d = 0; d < 7; d++) {
                semana.add(new Dia(data, data.getMonthValue() == periodo.getMonthValue(),
                    data.equals(LocalDate.now(java.time.ZoneId.of("America/Manaus"))), porDia.getOrDefault(data, List.of())));
                data = data.plusDays(1);
            }
            semanas.add(semana);
        }
        model.addAttribute("semanas", semanas);
        model.addAttribute("mesTitulo", periodo.getMonth().getDisplayName(
            java.time.format.TextStyle.FULL, new Locale("pt", "BR")) + " de " + periodo.getYear());
        model.addAttribute("anterior", periodo.minusMonths(1));
        model.addAttribute("proximo", periodo.plusMonths(1));
        return "calendario";
    }

    public static class Dia {
        private final LocalDate data;
        private final boolean noMes;
        private final boolean hoje;
        private final List<Tarefa> tarefas;

        public Dia(LocalDate data, boolean noMes, boolean hoje, List<Tarefa> tarefas) {
            this.data = data; this.noMes = noMes; this.hoje = hoje; this.tarefas = tarefas;
        }
        public LocalDate getData() { return data; }
        public boolean isNoMes() { return noMes; }
        public boolean isHoje() { return hoje; }
        public List<Tarefa> getTarefas() { return tarefas; }
    }
}
