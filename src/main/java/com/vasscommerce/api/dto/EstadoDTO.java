package com.vasscommerce.api.dto;

import com.vasscommerce.api.entity.Estado;

public class EstadoDTO {

    private Long id;
    private String sigla;
    private String nome;

    public EstadoDTO() {
    }

    public EstadoDTO(Estado estado) {
        this.id = estado.getId();
        this.sigla = estado.getSigla();
        this.nome = estado.getNome();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
