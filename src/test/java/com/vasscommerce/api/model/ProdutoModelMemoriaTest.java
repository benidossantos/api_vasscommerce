package com.vasscommerce.api.model;

import com.vasscommerce.api.dto.ProdutoCreateRequest;
import com.vasscommerce.api.entity.Produto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Teste unitario puro (sem Spring): a implementacao em memoria pode ser
 * instanciada e testada isoladamente, pois nao depende de nada externo.
 */
class ProdutoModelMemoriaTest {

    private final ProdutoModelMemoria model = new ProdutoModelMemoria();

    @Test
    void criarGeraIdsSequenciaisEPreencheDatas() {
        Produto p1 = model.criar(request("Mouse", "29.90"));
        Produto p2 = model.criar(request("Teclado", "99.90"));

        assertThat(p1.getId()).isEqualTo(1L);
        assertThat(p2.getId()).isEqualTo(2L);
        assertThat(p1.getDataCadastro()).isNotNull();
        assertThat(p1.getCategoria().getId()).isEqualTo(1L);
    }

    @Test
    void obterPorIdRetornaVazioQuandoNaoExiste() {
        Produto criado = model.criar(request("Mouse", "29.90"));

        assertThat(model.obterPorId(criado.getId())).contains(criado);
        assertThat(model.obterPorId(999L)).isEmpty();
    }

    @Test
    void buscarAplicaFiltrosDeNomeEValor() {
        model.criar(request("Mouse Gamer", "150.00"));
        model.criar(request("Mouse Simples", "29.90"));
        model.criar(request("Teclado", "99.90"));

        assertThat(nomes(model.buscar(null, null, null)))
                .containsExactly("Mouse Gamer", "Mouse Simples", "Teclado");
        assertThat(nomes(model.buscar("mouse", null, null)))
                .containsExactly("Mouse Gamer", "Mouse Simples");
        assertThat(nomes(model.buscar(null, new BigDecimal("50"), new BigDecimal("100"))))
                .containsExactly("Teclado");
        assertThat(nomes(model.buscar("MOUSE", new BigDecimal("100"), null)))
                .containsExactly("Mouse Gamer");
    }

    private static ProdutoCreateRequest request(String nome, String valor) {
        return new ProdutoCreateRequest(nome, "Descricao de " + nome, null, new BigDecimal(valor), 1L);
    }

    private static List<String> nomes(List<Produto> produtos) {
        return produtos.stream().map(Produto::getNome).toList();
    }
}
