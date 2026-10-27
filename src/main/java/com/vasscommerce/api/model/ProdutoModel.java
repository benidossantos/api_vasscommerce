package com.vasscommerce.api.model;

import com.vasscommerce.api.dto.ProdutoCreateRequest;
import com.vasscommerce.api.entity.Produto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Contrato da regra de negocio de Produto.
 *
 * O ProdutoController depende apenas desta interface; a implementacao concreta
 * (ProdutoModelMemoria ou ProdutoModelSql) e escolhida e injetada pelo Spring
 * (IoC/DI), sem que o controller precise conhece-la.
 */
public interface ProdutoModel {

    /** Regra de criacao de um novo produto. */
    Produto criar(ProdutoCreateRequest req);

    /** Consulta de um produto pelo id. */
    Optional<Produto> obterPorId(Long id);

    /**
     * Busca de produtos. Todos os filtros sao opcionais (null = sem filtro):
     * nome (contem, ignorando maiusculas/minusculas) e faixa de valor unitario.
     */
    List<Produto> buscar(String nome, BigDecimal valorMinimo, BigDecimal valorMaximo);
}
