package com.vasscommerce.api.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Comparator;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> tratarRecursoNaoEncontrado(ResourceNotFoundException ex,
                                                                 HttpServletRequest request) {
        return montarResposta(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    /**
     * 400 - falha no @Valid do corpo da requisicao (ex.: ProdutoCreateRequest).
     * Lista cada campo invalido e o motivo.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> tratarValidacao(MethodArgumentNotValidException ex,
                                                    HttpServletRequest request) {
        List<ApiError.CampoInvalido> campos = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ApiError.CampoInvalido(fe.getField(), fe.getDefaultMessage()))
                .sorted(Comparator.comparing(ApiError.CampoInvalido::getCampo))
                .toList();

        ApiError erro = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Dados invalidos na requisicao",
                request.getRequestURI(),
                campos
        );
        return ResponseEntity.badRequest().body(erro);
    }

    /**
     * 400 - corpo ausente ou JSON malformado (ex.: valorUnitario = "abc").
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> tratarCorpoInvalido(HttpMessageNotReadableException ex,
                                                        HttpServletRequest request) {
        return montarResposta(HttpStatus.BAD_REQUEST, "Corpo da requisicao ausente ou JSON malformado", request);
    }

    /**
     * 400 - parametro de rota/query com tipo invalido (ex.: /produto/abc ou valorMinimo=abc).
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> tratarParametroInvalido(MethodArgumentTypeMismatchException ex,
                                                            HttpServletRequest request) {
        String mensagem = "Parametro '" + ex.getName() + "' com valor invalido: '" + ex.getValue() + "'";
        return montarResposta(HttpStatus.BAD_REQUEST, mensagem, request);
    }

    /**
     * Demais erros. Excecoes do proprio Spring MVC (rota inexistente, metodo HTTP
     * nao suportado, Content-Type nao suportado...) ja carregam o status HTTP
     * correto (404, 405, 415...); qualquer outra excecao vira 500.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> tratarErroGenerico(Exception ex, HttpServletRequest request) {
        HttpStatusCode status = (ex instanceof ErrorResponse errorResponse)
                ? errorResponse.getStatusCode()
                : HttpStatus.INTERNAL_SERVER_ERROR;
        return montarResposta(status, ex.getMessage(), request);
    }

    private ResponseEntity<ApiError> montarResposta(HttpStatusCode status, String mensagem,
                                                    HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.resolve(status.value());
        String erro = httpStatus != null ? httpStatus.getReasonPhrase() : String.valueOf(status.value());

        ApiError apiError = new ApiError(status.value(), erro, mensagem, request.getRequestURI());
        return ResponseEntity.status(status).body(apiError);
    }
}
