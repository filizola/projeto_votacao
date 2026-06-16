package com.votacao.dto;

import jakarta.validation.constraints.Min;
import java.util.Optional;

public record AbrirSessaoRequest(
        @Min(value = 1, message = "duracaoEmMinutos deve ser no mínimo 1")
        Integer duracaoEmMinutos
) {

    public int duracaoEmMinutosOu(int padrao) {
        return Optional.ofNullable(duracaoEmMinutos).orElse(padrao);
    }

    public static int resolverDuracao(AbrirSessaoRequest request, int padrao) {
        return Optional.ofNullable(request)
                .map(r -> r.duracaoEmMinutosOu(padrao))
                .orElse(padrao);
    }
}
