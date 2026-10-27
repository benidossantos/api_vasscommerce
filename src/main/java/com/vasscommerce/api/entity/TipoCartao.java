package com.vasscommerce.api.entity;

/**
 * Enumeracao correspondente a classe de enumeracao "TipoCartao" do diagrama:
 * DEBITO = 1, CREDITO = 2.
 */
public enum TipoCartao {

    DEBITO(1),
    CREDITO(2);

    private final int codigo;

    TipoCartao(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }
}
