package com.vasscommerce.api.model;

import com.vasscommerce.api.dto.ProdutoCreateRequest;
import com.vasscommerce.api.entity.Produto;
import com.vasscommerce.api.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Prova de selecao por @Profile: com o profile "sql" ativo, o Spring injeta
 * ProdutoModelSql (banco H2 populado pelo DataSeeder).
 */
@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:${random.uuid};DB_CLOSE_DELAY=-1")
@ActiveProfiles("sql")
class SelecaoPorProfileSqlTest {

    @Autowired
    private ProdutoModel model;

    @Autowired
    private ApplicationContext context;

    @Test
    void injetaImplementacaoSql() {
        assertThat(model).isInstanceOf(ProdutoModelSql.class);
        assertThat(context.getBeansOfType(ProdutoModel.class)).containsOnlyKeys("produtoModelSql");
    }

    @Test
    void enxergaOsProdutosDoBanco() {
        assertThat(model.obterPorId(1L)).map(Produto::getNome).contains("Smartphone XZ200");
        assertThat(model.buscar("FONE", null, null)).extracting(Produto::getNome)
                .containsExactly("Fone de Ouvido Bluetooth");
        assertThat(model.buscar(null, new BigDecimal("100"), new BigDecimal("500"))).extracting(Produto::getNome)
                .containsExactly("Fone de Ouvido Bluetooth");
    }

    @Test
    void criarPersisteComCategoriaDoBanco() {
        Produto criado = model.criar(new ProdutoCreateRequest(
                "Kindle", "Leitor de livros digitais", null, new BigDecimal("499.00"), 2L));

        assertThat(criado.getId()).isNotNull();
        assertThat(criado.getCategoria().getNome()).isEqualTo("Livros");
        assertThat(model.obterPorId(criado.getId())).isPresent();
    }

    @Test
    void criarComCategoriaInexistenteFalha() {
        ProdutoCreateRequest req = new ProdutoCreateRequest(
                "Kindle", "Leitor de livros digitais", null, new BigDecimal("499.00"), 999L);

        assertThatThrownBy(() -> model.criar(req))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Categoria nao encontrada com id 999");
    }
}
