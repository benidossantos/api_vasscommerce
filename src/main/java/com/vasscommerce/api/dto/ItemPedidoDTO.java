package com.vasscommerce.api.dto;

import com.vasscommerce.api.entity.ItemPedido;

import java.math.BigDecimal;

public class ItemPedidoDTO {

    private Long id;
    private Long produtoId;
    private String produtoNome;
    private Integer quantidade;
    private BigDecimal valorUnitario;

    public ItemPedidoDTO() {
    }

    public ItemPedidoDTO(ItemPedido itemPedido) {
        this.id = itemPedido.getId();
        this.produtoId = itemPedido.getProduto().getId();
        this.produtoNome = itemPedido.getProduto().getNome();
        this.quantidade = itemPedido.getQuantidade();
        this.valorUnitario = itemPedido.getValorUnitario();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }
}
