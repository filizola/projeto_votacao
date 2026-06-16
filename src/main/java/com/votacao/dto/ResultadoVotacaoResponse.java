package com.votacao.dto;

import com.votacao.domain.ResultadoPauta;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Contagem de votos e resultado final de uma pauta")
public record ResultadoVotacaoResponse(

        @Schema(description = "Identificador da pauta", example = "1")
        Long pautaId,

        @Schema(description = "Total de votos registrados", example = "100")
        long totalVotos,

        @Schema(description = "Quantidade de votos SIM", example = "60")
        long votosSim,

        @Schema(description = "Quantidade de votos NAO", example = "40")
        long votosNao,

        @Schema(
                description = """
                        Resultado final da votação.
                        SIM = maioria favorável; NAO = maioria contrária; EMPATE = votosSim igual a votosNao.
                        """,
                example = "SIM"
        )
        ResultadoPauta resultado
) {
}
