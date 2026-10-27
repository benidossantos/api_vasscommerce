package com.vasscommerce.api.dto;

import com.vasscommerce.api.entity.Cliente;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ClienteDTO {

    private Long id;
    private String nomeCompleto;
    private String email;
    private String fotoUrl;
    private LocalDate dataNascimento;
    private String cpf;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataUltimaAtualizacao;

    public ClienteDTO() {
    }

    public ClienteDTO(Cliente cliente) {
        this.id = cliente.getId();
        this.nomeCompleto = cliente.getNomeCompleto();
        this.email = cliente.getEmail();
        this.fotoUrl = cliente.getFotoUrl();
        this.dataNascimento = cliente.getDataNascimento();
        this.cpf = cliente.getCpf();
        this.dataCadastro = cliente.getDataCadastro();
        this.dataUltimaAtualizacao = cliente.getDataUltimaAtualizacao();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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
