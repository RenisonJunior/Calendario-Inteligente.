package br.com.prioriza.service;

import br.com.prioriza.model.*;
import br.com.prioriza.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class RotinaService {
    private final TarefaRepository tarefas;
    private final SubtarefaRepository subtarefas;
    private final RegistroFocoRepository focos;
    private final PerfilRepository perfis;

    public RotinaService(TarefaRepository tarefas, SubtarefaRepository subtarefas,
                         RegistroFocoRepository focos, PerfilRepository perfis) {
        this.tarefas = tarefas; this.subtarefas = subtarefas;
        this.focos = focos; this.perfis = perfis;
    }

    public Tarefa encontrar(Long id) {
        return tarefas.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"));
    }

    public Perfil perfil() {
        return perfis.findAll().stream().findFirst().orElseThrow(() ->
            new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Perfil não cadastrado"));
    }

    // Prioridade calculada automaticamente de acordo com o prazo.
    public String prioridade(Tarefa tarefa) {
        if (tarefa.isConcluida()) return "Concluída";
        long dias = ChronoUnit.DAYS.between(LocalDate.now(java.time.ZoneId.of("America/Manaus")), tarefa.getPrazo());
        if (dias < 0) return "Atrasada";
        if (dias == 0) return "Hoje";
        if (dias <= 2) return "Alta";
        if (dias <= 7) return "Média";
        return "Baixa";
    }

    // Planejamento automático: tarefas urgentes primeiro, respeitando o tempo disponível.
    public List<LinhaPainel> painel() {
        List<Tarefa> lista = new ArrayList<>(tarefas.findAll());
        lista.sort(Comparator.comparing(Tarefa::isConcluida)
            .thenComparing(Tarefa::getPrazo)
            .thenComparing(Tarefa::getMinutosEstimados));
        int saldo = perfil().getMinutosDisponiveis();
        List<LinhaPainel> linhas = new ArrayList<>();
        for (Tarefa tarefa : lista) {
            String sugestao;
            if (tarefa.isConcluida()) sugestao = "Finalizada";
            else if (tarefa.getMinutosEstimados() <= saldo) {
                sugestao = "Fazer hoje";
                saldo -= tarefa.getMinutosEstimados();
            } else sugestao = "Próximo dia";
            linhas.add(new LinhaPainel(tarefa, prioridade(tarefa), sugestao));
        }
        return linhas;
    }

    // Percentual de etapas concluídas sem precisar preencher manualmente.
    public int progresso(Long tarefaId) {
        List<Subtarefa> lista = subtarefas.findByTarefaIdOrderByIdAsc(tarefaId);
        if (lista.isEmpty()) return 0;
        long completas = lista.stream().filter(Subtarefa::isConcluida).count();
        return (int) (completas * 100 / lista.size());
    }

    // Soma automática de minutos dedicados a uma tarefa.
    public int minutosFoco(Long tarefaId) {
        return focos.findByTarefaIdOrderByIdAsc(tarefaId).stream()
            .mapToInt(RegistroFoco::getMinutos).sum();
    }

    public static class LinhaPainel {
        private final Tarefa tarefa;
        private final String prioridade;
        private final String sugestao;

        public LinhaPainel(Tarefa tarefa, String prioridade, String sugestao) {
            this.tarefa = tarefa; this.prioridade = prioridade; this.sugestao = sugestao;
        }
        public Tarefa getTarefa() { return tarefa; }
        public String getPrioridade() { return prioridade; }
        public String getSugestao() { return sugestao; }
    }
}
