package com.vasscommerce.api.dto;

import com.vasscommerce.api.entity.Endereco;

import java.time.LocalDateTime;

public class EnderecoDTO {

    private Long id;
    private String rua;
    private Integer numero;
    private String cep;
    private String complemento;
    private String telefone;
    private String bairro;
    private CidadeDTO cidade;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataUltimaAtualizacao;

    public EnderecoDTO() {
    }

    public EnderecoDTO(Endereco endereco) {
        this.id = endereco.getId();
        this.rua = endereco.getRua();
        this.numero = endereco.getNumero();
        this.cep = endereco.getCep();
        this.complemento = endereco.getComplemento();
        this.telefone = endereco.getTelefone();
        this.bairro = endereco.getBairro();
        this.cidade = new CidadeDTO(endereco.getCidade());
        this.dataCadastro = endereco.getDataCadastro();
        this.dataUltimaAtualizacao = endereco.getDataUltimaAtualizacao();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public CidadeDTO getCidade() {
        return cidade;
    }

    public void setCidade(CidadeDTO cidade) {
        this.cidade = cidade;
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
