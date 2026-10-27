package com.vasscommerce.api.dto;

import com.vasscommerce.api.entity.Cartao;

import java.time.LocalDateTime;

public class CartaoDTO {

    private Long id;
    private String tipo;
    private Boolean excluido;
    private LocalDateTime dataCriacao;

    public CartaoDTO() {
    }

    public CartaoDTO(Cartao cartao) {
        this.id = cartao.getId();
        this.tipo = cartao.getTipo().name();
        this.excluido = cartao.getExcluido();
        this.dataCriacao = cartao.getDataCriacao();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean getExcluido() {
        return excluido;
    }

    public void setExcluido(Boolean excluido) {
        this.excluido = excluido;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
