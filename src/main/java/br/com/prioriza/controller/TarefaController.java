package br.com.prioriza.controller;

import br.com.prioriza.model.*;
import br.com.prioriza.repository.*;
import br.com.prioriza.service.RotinaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;

@Controller
@RequestMapping("/tarefas")
public class TarefaController {
    private final TarefaRepository tarefas;
    private final CategoriaRepository categorias;
    private final SubtarefaRepository subtarefas;
    private final RegistroFocoRepository focos;
    private final RotinaService rotina;

    public TarefaController(TarefaRepository tarefas, CategoriaRepository categorias,
            SubtarefaRepository subtarefas, RegistroFocoRepository focos, RotinaService rotina) {
        this.tarefas = tarefas; this.categorias = categorias;
        this.subtarefas = subtarefas; this.focos = focos; this.rotina = rotina;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("tarefas", tarefas.findAllByOrderByPrazoAsc());
        return "tarefas";
    }

    @GetMapping("/nova")
    public String nova(Model model) {
        model.addAttribute("tarefa", new Tarefa());
        model.addAttribute("categorias", categorias.findAllByOrderByNomeAsc());
        model.addAttribute("hoje", LocalDate.now(java.time.ZoneId.of("America/Manaus")));
        return "tarefa-form";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("tarefa", rotina.encontrar(id));
        model.addAttribute("categorias", categorias.findAllByOrderByNomeAsc());
        model.addAttribute("hoje", LocalDate.now(java.time.ZoneId.of("America/Manaus")));
        return "tarefa-form";
    }

    @PostMapping("/salvar")
    public String salvar(@RequestParam(required = false) Long id,
            @RequestParam String titulo, @RequestParam(defaultValue = "") String descricao,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate prazo,
            @RequestParam Long categoriaId, @RequestParam int minutosEstimados,
            RedirectAttributes mensagens) {
        if (titulo.isBlank() || titulo.length() > 120 || descricao.length() > 800
                || minutosEstimados < 1 || minutosEstimados > 1440) {
            mensagens.addFlashAttribute("erro", "Preencha os campos corretamente (tempo entre 1 e 1440 minutos).");
            return id == null ? "redirect:/tarefas/nova" : "redirect:/tarefas/" + id + "/editar";
        }
        Categoria categoria = categorias.findById(categoriaId).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria inexistente"));
        Tarefa tarefa = id == null ? new Tarefa() : rotina.encontrar(id);
        tarefa.setTitulo(titulo.trim());
        tarefa.setDescricao(descricao.trim());
        tarefa.setPrazo(prazo);
        tarefa.setCategoria(categoria);
        tarefa.setMinutosEstimados(minutosEstimados);
        tarefas.save(tarefa);
        return "redirect:/tarefas";
    }

    @GetMapping("/{id}")
    public String detalhe(@PathVariable Long id, Model model) {
        Tarefa tarefa = rotina.encontrar(id);
        model.addAttribute("tarefa", tarefa);
        model.addAttribute("subtarefas", subtarefas.findByTarefaIdOrderByIdAsc(id));
        model.addAttribute("progresso", rotina.progresso(id));
        model.addAttribute("minutosFoco", rotina.minutosFoco(id));
        model.addAttribute("prioridade", rotina.prioridade(tarefa));
        return "tarefa-detalhe";
    }

    @PostMapping("/{id}/alternar")
    public String alternar(@PathVariable Long id) {
        Tarefa tarefa = rotina.encontrar(id);
        tarefa.setConcluida(!tarefa.isConcluida());
        tarefas.save(tarefa);
        return "redirect:/tarefas";
    }

    @Transactional
    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id) {
        Tarefa tarefa = rotina.encontrar(id);
        // Primeiro removemos os registros filhos, respeitando as foreign keys.
        subtarefas.deleteByTarefaId(id);
        focos.deleteByTarefaId(id);
        tarefas.delete(tarefa);
        return "redirect:/tarefas";
    }

    @PostMapping("/{id}/subtarefas")
    public String novaSubtarefa(@PathVariable Long id, @RequestParam String titulo) {
        Tarefa tarefa = rotina.encontrar(id);
        if (!titulo.isBlank() && titulo.length() <= 120) {
            Subtarefa sub = new Subtarefa();
            sub.setTitulo(titulo.trim());
            sub.setTarefa(tarefa);
            subtarefas.save(sub);
        }
        return "redirect:/tarefas/" + id;
    }

    @PostMapping("/{id}/subtarefas/{subId}/alternar")
    public String alternarSubtarefa(@PathVariable Long id, @PathVariable Long subId) {
        rotina.encontrar(id);
        Subtarefa sub = subtarefas.findById(subId).filter(s -> s.getTarefa().getId().equals(id))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        sub.setConcluida(!sub.isConcluida());
        subtarefas.save(sub);
        return "redirect:/tarefas/" + id;
    }

    @PostMapping("/{id}/subtarefas/{subId}/excluir")
    public String excluirSubtarefa(@PathVariable Long id, @PathVariable Long subId) {
        rotina.encontrar(id);
        Subtarefa sub = subtarefas.findById(subId).filter(s -> s.getTarefa().getId().equals(id))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        subtarefas.delete(sub);
        return "redirect:/tarefas/" + id;
    }

    @PostMapping("/{id}/foco")
    public String registrarFoco(@PathVariable Long id, @RequestParam int minutos) {
        Tarefa tarefa = rotina.encontrar(id);
        if (minutos < 1 || minutos > 1440) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tempo entre 1 e 1440 minutos");
        }
        RegistroFoco registro = new RegistroFoco();
        registro.setTarefa(tarefa);
        registro.setData(LocalDate.now(java.time.ZoneId.of("America/Manaus")));
        registro.setMinutos(minutos);
        focos.save(registro);
        return "redirect:/tarefas/" + id;
    }
}
