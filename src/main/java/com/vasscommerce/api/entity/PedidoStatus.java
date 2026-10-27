package com.vasscommerce.api.entity;

/**
 * Enumeracao correspondente a classe de enumeracao "PedidoStatus" do diagrama:
 * AGUARDANDO_PAGAMENTO = 1, SEPARANDO_ESTOQUE = 2,
 * ENTREGUE_TRANSPORTADORA = 3, ENTREGUE_CLIENTE = 4.
 */
public enum PedidoStatus {

    AGUARDANDO_PAGAMENTO(1),
    SEPARANDO_ESTOQUE(2),
    ENTREGUE_TRANSPORTADORA(3),
    ENTREGUE_CLIENTE(4);

    private final int codigo;

    PedidoStatus(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }
}
