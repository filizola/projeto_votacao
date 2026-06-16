package com.votacao.dto;

import java.time.Instant;
import org.springframework.http.HttpStatus;

public record ErroResponse(int status, String mensagem, Instant timestamp) {

    public static ErroResponse de(HttpStatus httpStatus, String mensagem) {
        return new ErroResponse(httpStatus.value(), mensagem, Instant.now());
    }
}
