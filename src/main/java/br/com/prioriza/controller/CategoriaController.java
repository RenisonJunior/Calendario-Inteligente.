package br.com.prioriza.controller;

import br.com.prioriza.model.Categoria;
import br.com.prioriza.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {
    private final CategoriaRepository categorias;
    private final TarefaRepository tarefas;
    public CategoriaController(CategoriaRepository categorias, TarefaRepository tarefas) {
        this.categorias = categorias; this.tarefas = tarefas;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias", categorias.findAllByOrderByNomeAsc());
        return "categorias";
    }

    @PostMapping
    public String criar(@RequestParam String nome, RedirectAttributes mensagens) {
        if (nome.isBlank() || nome.length() > 60) {
            mensagens.addFlashAttribute("erro", "Informe um nome de até 60 caracteres.");
        } else {
            Categoria categoria = new Categoria();
            categoria.setNome(nome.trim());
            categorias.save(categoria);
        }
        return "redirect:/categorias";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes mensagens) {
        Categoria categoria = categorias.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (tarefas.existsByCategoriaId(id)) {
            // Não deixa apagar a categoria enquanto alguma tarefa a utiliza.
            mensagens.addFlashAttribute("erro", "Esta categoria tem tarefas. Mova ou exclua as tarefas antes.");
        } else categorias.delete(categoria);
        return "redirect:/categorias";
    }
}
