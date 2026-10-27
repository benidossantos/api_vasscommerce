package com.vasscommerce.api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String descricao;

    private String imagemSimboloUrl;

    public Categoria() {
    }

    public Categoria(String nome, String descricao, String imagemSimboloUrl) {
        this.nome = nome;
        this.descricao = descricao;
        this.imagemSimboloUrl = imagemSimboloUrl;
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
