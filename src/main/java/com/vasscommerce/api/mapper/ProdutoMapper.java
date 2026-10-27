package com.vasscommerce.api.mapper;

import com.vasscommerce.api.dto.ProdutoCreateRequest;
import com.vasscommerce.api.dto.ProdutoResponse;
import com.vasscommerce.api.entity.Categoria;
import com.vasscommerce.api.entity.Produto;

/**
 * Conversoes entre os DTOs de Produto e a entidade Produto:
 * Request DTO -> Entidade e Entidade -> Response DTO.
 */
public final class ProdutoMapper {

    private ProdutoMapper() {
    }

    /**
     * Cria a entidade a partir do request. A categoria e apenas uma referencia
     * pelo id informado; a implementacao de ProdutoModel decide se precisa
     * carregar/validar a categoria completa.
     */
    public static Produto toEntity(ProdutoCreateRequest req, Long id) {
        Categoria categoria = new Categoria();
        categoria.setId(req.getCategoriaId());

        Produto p = new Produto();
        p.setId(id);
        p.setNome(req.getNome().trim());
        p.setDescricao(req.getDescricao().trim());
        p.setFotoUrl(req.getFotoUrl());
        p.setValorUnitario(req.getValorUnitario());
        p.setCategoria(categoria);
        return p;
    }

    public static ProdutoResponse toResponse(Produto p) {
        ProdutoResponse r = new ProdutoResponse();
        r.setId(p.getId());
        r.setNome(p.getNome());
        r.setDescricao(p.getDescricao());
        r.setFotoUrl(p.getFotoUrl());
        r.setValorUnitario(p.getValorUnitario());
        if (p.getCategoria() != null) {
            r.setCategoriaId(p.getCategoria().getId());
            r.setCategoriaNome(p.getCategoria().getNome());
        }
        r.setDataCadastro(p.getDataCadastro());
        r.setDataUltimaAtualizacao(p.getDataUltimaAtualizacao());
        return r;
    }
}
