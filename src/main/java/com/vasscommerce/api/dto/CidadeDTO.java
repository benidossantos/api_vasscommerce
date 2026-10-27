package com.vasscommerce.api.dto;

import com.vasscommerce.api.entity.Cidade;

public class CidadeDTO {

    private Long id;
    private String nome;
    private EstadoDTO estado;

    public CidadeDTO() {
    }

    public CidadeDTO(Cidade cidade) {
        this.id = cidade.getId();
        this.nome = cidade.getNome();
        this.estado = new EstadoDTO(cidade.getEstado());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public EstadoDTO getEstado() {
        return estado;
    }

    public void setEstado(EstadoDTO estado) {
        this.estado = estado;
    }
}
