package com.vasscommerce.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Sobe a aplicacao inteira (profile sql) e garante que as rotas GET das etapas
 * anteriores continuam funcionando.
 */
@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:${random.uuid};DB_CLOSE_DELAY=-1")
@AutoConfigureMockMvc
@ActiveProfiles("sql")
class ApiIntegracaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void rotasDeLeituraRespondem200() throws Exception {
        mockMvc.perform(get("/produto/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.categoriaNome").value("Eletronicos"));
        mockMvc.perform(get("/categoria")).andExpect(status().isOk());
        mockMvc.perform(get("/categoria").param("nome", "eletro")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Eletronicos"));
        mockMvc.perform(get("/categoria/1/produto")).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
        mockMvc.perform(get("/cliente/1")).andExpect(status().isOk());
        mockMvc.perform(get("/cliente/1/formas-de-pagamento")).andExpect(status().isOk());
        mockMvc.perform(get("/cliente/1/endereco")).andExpect(status().isOk())
                .andExpect(jsonPath("$.cidade.estado.sigla").value("RJ"));
        mockMvc.perform(get("/cliente/1/pedido")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].itens.length()").value(2));
        mockMvc.perform(get("/tipo-cartao")).andExpect(status().isOk());
        mockMvc.perform(get("/estado")).andExpect(status().isOk());
        mockMvc.perform(get("/estado/1/cidade")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado.sigla").value("RJ"));
    }

    @Test
    void recursosInexistentesRespondem404() throws Exception {
        mockMvc.perform(get("/produto/999")).andExpect(status().isNotFound());
        mockMvc.perform(get("/cliente/999")).andExpect(status().isNotFound());
        mockMvc.perform(get("/rota-que-nao-existe")).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
