package com.vasscommerce.api.controller;

import com.vasscommerce.api.dto.ProdutoCreateRequest;
import com.vasscommerce.api.entity.Categoria;
import com.vasscommerce.api.entity.Produto;
import com.vasscommerce.api.model.ProdutoModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testa o contrato HTTP do ProdutoController isoladamente. Como o controller
 * depende apenas da interface ProdutoModel, ela e substituida por um mock,
 * sem precisar de nenhuma das implementacoes concretas.
 */
@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoModel model;

    @Test
    void postValidoRetorna201ComLocation() throws Exception {
        when(model.criar(any(ProdutoCreateRequest.class))).thenReturn(produto(10L, "Mouse"));

        mockMvc.perform(post("/produto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Mouse","descricao":"Mouse sem fio","valorUnitario":59.90,"categoriaId":1}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/produto/10")))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.nome").value("Mouse"))
                .andExpect(jsonPath("$.categoriaId").value(1));
    }

    @Test
    void postInvalidoRetorna400PadronizadoSemChamarOModel() throws Exception {
        mockMvc.perform(post("/produto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"","valorUnitario":-5}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erro").value("Bad Request"))
                .andExpect(jsonPath("$.mensagem").value("Dados invalidos na requisicao"))
                .andExpect(jsonPath("$.caminho").value("/produto"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.campos[*].campo").value(
                        org.hamcrest.Matchers.containsInAnyOrder("categoriaId", "descricao", "nome", "valorUnitario")));

        verifyNoInteractions(model);
    }

    @Test
    void postComJsonMalformadoRetorna400() throws Exception {
        mockMvc.perform(post("/produto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"Mouse\", \"valorUnitario\": \"abc\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("Corpo da requisicao ausente ou JSON malformado"));
    }

    @Test
    void postComContentTypeErradoRetorna415() throws Exception {
        mockMvc.perform(post("/produto")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("qualquer coisa"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.status").value(415));
    }

    @Test
    void getExistenteRetorna200() throws Exception {
        when(model.obterPorId(10L)).thenReturn(Optional.of(produto(10L, "Mouse")));

        mockMvc.perform(get("/produto/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.nome").value("Mouse"));
    }

    @Test
    void getInexistenteRetorna404Padronizado() throws Exception {
        when(model.obterPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/produto/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.mensagem").value("Produto nao encontrado com id 99"))
                .andExpect(jsonPath("$.caminho").value("/produto/99"));
    }

    @Test
    void getComIdNaoNumericoRetorna400() throws Exception {
        mockMvc.perform(get("/produto/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void listRepassaOsFiltrosParaOModel() throws Exception {
        when(model.buscar("mou", new BigDecimal("10"), new BigDecimal("100")))
                .thenReturn(List.of(produto(10L, "Mouse")));

        mockMvc.perform(get("/produto")
                        .param("nome", "mou")
                        .param("valorMinimo", "10")
                        .param("valorMaximo", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nome").value("Mouse"));

        verify(model).buscar("mou", new BigDecimal("10"), new BigDecimal("100"));
    }

    private static Produto produto(Long id, String nome) {
        Categoria categoria = new Categoria("Eletronicos", "Produtos eletronicos", null);
        categoria.setId(1L);

        Produto p = new Produto();
        p.setId(id);
        p.setNome(nome);
        p.setDescricao("Mouse sem fio");
        p.setValorUnitario(new BigDecimal("59.90"));
        p.setCategoria(categoria);
        return p;
    }
}
