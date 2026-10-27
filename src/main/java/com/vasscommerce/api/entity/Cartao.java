package com.vasscommerce.api.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cartao")
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCartao tipo;

    @Column(nullable = false)
    private Boolean excluido = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente titular;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    protected void aoCriar() {
        this.dataCriacao = LocalDateTime.now();
    }

    public Cartao() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoCartao getTipo() {
        return tipo;
    }

    public void setTipo(TipoCartao tipo) {
        this.tipo = tipo;
    }

    public Boolean getExcluido() {
        return excluido;
    }

    public void setExcluido(Boolean excluido) {
        this.excluido = excluido;
    }

    public Cliente getTitular() {
        return titular;
    }

    public void setTitular(Cliente titular) {
        this.titular = titular;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
}
