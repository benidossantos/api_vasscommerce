package com.vasscommerce.api.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Prova de selecao por @Profile: com o profile "mem" ativo, o Spring injeta
 * ProdutoModelMemoria e o bean ProdutoModelSql nem chega a ser criado.
 */
@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:${random.uuid};DB_CLOSE_DELAY=-1")
@ActiveProfiles("mem")
class SelecaoPorProfileMemTest {

    @Autowired
    private ProdutoModel model;

    @Autowired
    private ApplicationContext context;

    @Test
    void injetaImplementacaoEmMemoria() {
        assertThat(model).isInstanceOf(ProdutoModelMemoria.class);
        assertThat(context.getBeansOfType(ProdutoModel.class)).containsOnlyKeys("produtoModelMemoria");
    }
}
