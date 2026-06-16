package com.votacao.exception;

import com.votacao.dto.AutorizacaoVotoResponse;
import com.votacao.dto.ErroResponse;
import com.votacao.dto.StatusAutorizacaoVoto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> handleRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErroResponse.de(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(AssociadoNaoAutorizadoException.class)
    public ResponseEntity<AutorizacaoVotoResponse> handleAssociadoNaoAutorizado(AssociadoNaoAutorizadoException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new AutorizacaoVotoResponse(StatusAutorizacaoVoto.UNABLE_TO_VOTE));
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResponse> handleRegraNegocio(RegraNegocioException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ErroResponse.de(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> handleIntegridade(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ErroResponse.de(HttpStatus.UNPROCESSABLE_ENTITY, "Associado já votou nesta pauta"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidacao(MethodArgumentNotValidException ex) {
        String mensagem = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Requisição inválida");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErroResponse.de(HttpStatus.BAD_REQUEST, mensagem));
    }
}
