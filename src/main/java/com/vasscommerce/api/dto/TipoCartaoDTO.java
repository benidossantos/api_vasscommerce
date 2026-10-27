package com.vasscommerce.api.dto;

import com.vasscommerce.api.entity.TipoCartao;

public class TipoCartaoDTO {

    private Integer id;
    private String descricao;

    public TipoCartaoDTO() {
    }

    public TipoCartaoDTO(TipoCartao tipoCartao) {
        this.id = tipoCartao.getCodigo();
        this.descricao = tipoCartao.name();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
