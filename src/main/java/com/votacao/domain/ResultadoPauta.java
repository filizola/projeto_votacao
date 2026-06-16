package com.votacao.domain;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Resultado final da votação em uma pauta",
        enumAsRef = true
)
public enum ResultadoPauta {

    @Schema(description = "Maioria dos votos foi favorável (SIM)")
    SIM,

    @Schema(description = "Maioria dos votos foi contrária (NAO)")
    NAO,

    @Schema(description = "Empate — quantidade de votos SIM e NAO é igual")
    EMPATE;

    public static ResultadoPauta calcular(long votosSim, long votosNao) {
        return switch (Long.compare(votosSim, votosNao)) {
            case 1 -> SIM;
            case -1 -> NAO;
            default -> EMPATE;
        };
    }
}
