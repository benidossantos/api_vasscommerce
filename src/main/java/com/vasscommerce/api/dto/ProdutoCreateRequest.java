package com.vasscommerce.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Corpo da requisicao POST /produto. As anotacoes de Bean Validation sao
 * verificadas pelo @Valid no controller; se alguma falhar, a API responde 400
 * com o JSON de erro padronizado (GlobalExceptionHandler).
 */
public class ProdutoCreateRequest {

    @NotBlank(message = "nome e obrigatorio")
    @Size(max = 255, message = "nome deve ter no maximo 255 caracteres")
    private String nome;

    @NotBlank(message = "descricao e obrigatoria")
    @Size(max = 1000, message = "descricao deve ter no maximo 1000 caracteres")
    private String descricao;

    @Size(max = 255, message = "fotoUrl deve ter no maximo 255 caracteres")
    private String fotoUrl;

    @NotNull(message = "valorUnitario e obrigatorio")
    @DecimalMin(value = "0.01", message = "valorUnitario deve ser maior que zero")
    @Digits(integer = 10, fraction = 2, message = "valorUnitario deve ter no maximo 2 casas decimais")
    private BigDecimal valorUnitario;

    @NotNull(message = "categoriaId e obrigatorio")
    @Positive(message = "categoriaId deve ser positivo")
    private Long categoriaId;

    public ProdutoCreateRequest() {
    }

    public ProdutoCreateRequest(String nome, String descricao, String fotoUrl,
                                BigDecimal valorUnitario, Long categoriaId) {
        this.nome = nome;
        this.descricao = descricao;
        this.fotoUrl = fotoUrl;
        this.valorUnitario = valorUnitario;
        this.categoriaId = categoriaId;
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

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }
}
