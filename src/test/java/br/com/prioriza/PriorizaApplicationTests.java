package br.com.prioriza;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PriorizaApplicationTests {
    @Autowired private MockMvc mvc;

    // Testa a abertura das páginas principais, incluindo o processamento Thymeleaf.
    @Test
    void paginasPrincipaisAbrem() throws Exception {
        for (String pagina : new String[]{"/", "/painel", "/calendario", "/tarefas", "/categorias", "/perfil"}) {
            mvc.perform(get(pagina)).andExpect(status().isOk());
        }
    }
}
