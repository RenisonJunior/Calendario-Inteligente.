package br.com.prioriza.controller;

import br.com.prioriza.model.Perfil;
import br.com.prioriza.repository.PerfilRepository;
import br.com.prioriza.service.RotinaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PerfilController {
    private final RotinaService rotina;
    private final PerfilRepository perfis;
    public PerfilController(RotinaService rotina, PerfilRepository perfis) {
        this.rotina = rotina; this.perfis = perfis;
    }

    @GetMapping("/perfil")
    public String perfil(Model model) {
        model.addAttribute("perfil", rotina.perfil());
        return "perfil";
    }

    @PostMapping("/perfil")
    public String salvar(@RequestParam String nome, @RequestParam int minutosDisponiveis,
                         RedirectAttributes mensagens) {
        if (nome.isBlank() || nome.length() > 80 || minutosDisponiveis < 1 || minutosDisponiveis > 1440) {
            mensagens.addFlashAttribute("erro", "Informe um nome e tempo entre 1 e 1440 minutos.");
        } else {
            Perfil perfil = rotina.perfil();
            perfil.setNome(nome.trim());
            perfil.setMinutosDisponiveis(minutosDisponiveis);
            perfis.save(perfil);
            mensagens.addFlashAttribute("ok", "Perfil atualizado.");
        }
        return "redirect:/perfil";
    }
}
