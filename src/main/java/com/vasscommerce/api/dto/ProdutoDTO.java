package com.vasscommerce.api.dto;

import com.vasscommerce.api.entity.Produto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProdutoDTO {

    private Long id;
    private String nome;
    private String descricao;
    private String fotoUrl;
    private BigDecimal valorUnitario;
    private CategoriaDTO categoria;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataUltimaAtualizacao;

    public ProdutoDTO() {
    }

    public ProdutoDTO(Produto produto) {
        this.id = produto.getId();
        this.nome = produto.getNome();
        this.descricao = produto.getDescricao();
        this.fotoUrl = produto.getFotoUrl();
        this.valorUnitario = produto.getValorUnitario();
        this.categoria = new CategoriaDTO(produto.getCategoria());
        this.dataCadastro = produto.getDataCadastro();
        this.dataUltimaAtualizacao = produto.getDataUltimaAtualizacao();
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

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public CategoriaDTO getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaDTO categoria) {
        this.categoria = categoria;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDateTime getDataUltimaAtualizacao() {
        return dataUltimaAtualizacao;
    }

    public void setDataUltimaAtualizacao(LocalDateTime dataUltimaAtualizacao) {
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
    }
}
