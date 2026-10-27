package com.vasscommerce.api.model;

import com.vasscommerce.api.dto.ProdutoCreateRequest;
import com.vasscommerce.api.entity.Categoria;
import com.vasscommerce.api.entity.Produto;
import com.vasscommerce.api.exception.ResourceNotFoundException;
import com.vasscommerce.api.mapper.ProdutoMapper;
import com.vasscommerce.api.repository.CategoriaRepository;
import com.vasscommerce.api.repository.ProdutoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Implementacao de ProdutoModel que representa um backend de banco de dados
 * relacional. Usa o Spring Data JPA sobre o H2 em memoria do projeto (o mesmo
 * codigo funcionaria com Postgres/MySQL trocando apenas o datasource).
 *
 * Ativa quando o profile "sql" esta ativo (spring.profiles.active=sql).
 */
@Service("produtoModelSql")
@Profile("sql")
public class ProdutoModelSql implements ProdutoModel {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoModelSql(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    @Transactional
    public Produto criar(ProdutoCreateRequest req) {
        // Regra de negocio: o produto precisa pertencer a uma categoria existente
        Categoria categoria = categoriaRepository.findById(req.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoria nao encontrada com id " + req.getCategoriaId()));

        Produto p = ProdutoMapper.toEntity(req, null);  // id gerado pelo banco
        p.setCategoria(categoria);
        return produtoRepository.save(p);
    }

    @Override
    public Optional<Produto> obterPorId(Long id) {
        return produtoRepository.findWithCategoriaById(id);
    }

    @Override
    public List<Produto> buscar(String nome, BigDecimal valorMinimo, BigDecimal valorMaximo) {
        return produtoRepository.buscar(nome, valorMinimo, valorMaximo);
    }
}
