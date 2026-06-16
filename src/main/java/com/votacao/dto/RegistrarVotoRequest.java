package com.votacao.dto;

import com.votacao.domain.OpcaoVoto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistrarVotoRequest(
        @NotBlank(message = "associadoId é obrigatório")
        String associadoId,

        @NotNull(message = "voto é obrigatório")
        OpcaoVoto voto,

        String cpf
) {
}
