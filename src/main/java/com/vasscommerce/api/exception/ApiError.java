package com.vasscommerce.api.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

public class ApiError {

    private LocalDateTime timestamp;
    private int status;
    private String erro;
    private String mensagem;
    private String caminho;

    // Preenchido apenas nos erros de validacao (400)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<CampoInvalido> campos;

    public ApiError(int status, String erro, String mensagem, String caminho) {
        this(status, erro, mensagem, caminho, List.of());
    }

    public ApiError(int status, String erro, String mensagem, String caminho, List<CampoInvalido> campos) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.erro = erro;
        this.mensagem = mensagem;
        this.caminho = caminho;
        this.campos = campos;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getErro() {
        return erro;
    }

    public String getMensagem() {
        return mensagem;
    }

    public String getCaminho() {
        return caminho;
    }

    public List<CampoInvalido> getCampos() {
        return campos;
    }

    public static class CampoInvalido {

        private final String campo;
        private final String mensagem;

        public CampoInvalido(String campo, String mensagem) {
            this.campo = campo;
            this.mensagem = mensagem;
        }

        public String getCampo() {
            return campo;
        }

        public String getMensagem() {
            return mensagem;
        }
    }
}
