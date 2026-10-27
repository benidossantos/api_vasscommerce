package com.vasscommerce.api.dto;

import com.vasscommerce.api.entity.Pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PedidoDTO {

    private Long id;
    private String statusAtual;
    private BigDecimal valorTotal;
    private LocalDateTime dataCadastro;
    private List<ItemPedidoDTO> itens;

    public PedidoDTO() {
    }

    public PedidoDTO(Pedido pedido) {
        this.id = pedido.getId();
        this.statusAtual = pedido.getStatusAtual().name();
        this.valorTotal = pedido.getValorTotal();
        this.dataCadastro = pedido.getDataCadastro();
        this.itens = pedido.getItens().stream()
                .map(ItemPedidoDTO::new)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatusAtual() {
        return statusAtual;
    }

    public void setStatusAtual(String statusAtual) {
        this.statusAtual = statusAtual;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public List<ItemPedidoDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoDTO> itens) {
        this.itens = itens;
    }
}
