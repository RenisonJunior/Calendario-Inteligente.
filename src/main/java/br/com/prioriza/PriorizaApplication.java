package br.com.prioriza;

import br.com.prioriza.model.Categoria;
import br.com.prioriza.model.Perfil;
import br.com.prioriza.repository.CategoriaRepository;
import br.com.prioriza.repository.PerfilRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PriorizaApplication {
    public static void main(String[] args) {
        SpringApplication.run(PriorizaApplication.class, args);
    }

    @Bean
    CommandLineRunner iniciar(CategoriaRepository categorias, PerfilRepository perfis) {
        // Cria alguns dados básicos somente na primeira execução.
        return args -> {
            if (categorias.count() == 0) {
                for (String nome : new String[]{"Estudos", "Trabalho", "Pessoal"}) {
                    Categoria categoria = new Categoria();
                    categoria.setNome(nome);
                    categorias.save(categoria);
                }
            }
            if (perfis.count() == 0) {
                Perfil perfil = new Perfil();
                perfil.setNome("Meu perfil");
                perfil.setMinutosDisponiveis(120);
                perfis.save(perfil);
            }
        };
    }
}
