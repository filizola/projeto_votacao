package com.votacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CriarPautaRequest(
        @NotBlank(message = "titulo é obrigatório")
        @Size(max = PautaConstraints.TITULO_MAX_LENGTH, message = "titulo deve ter no máximo 255 caracteres")
        String titulo,

        @Size(max = PautaConstraints.DESCRICAO_MAX_LENGTH, message = "descricao deve ter no máximo 2000 caracteres")
        String descricao
) {
}
