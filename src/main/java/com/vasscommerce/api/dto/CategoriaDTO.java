package com.vasscommerce.api.dto;

import com.vasscommerce.api.entity.Categoria;

public class CategoriaDTO {

    private Long id;
    private String nome;
    private String descricao;
    private String imagemSimboloUrl;

    public CategoriaDTO() {
    }

    public CategoriaDTO(Categoria categoria) {
        this.id = categoria.getId();
        this.nome = categoria.getNome();
        this.descricao = categoria.getDescricao();
        this.imagemSimboloUrl = categoria.getImagemSimboloUrl();
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImagemSimboloUrl() {
        return imagemSimboloUrl;
    }

    public void setImagemSimboloUrl(String imagemSimboloUrl) {
        this.imagemSimboloUrl = imagemSimboloUrl;
    }
}
