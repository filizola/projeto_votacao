package com.votacao.controller.v1;

import com.votacao.dto.ResultadoVotacaoResponse;
import com.votacao.service.ResultadoVotacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pautas/{pautaId}/resultado")
@Tag(name = "Resultado da votação")
@RequiredArgsConstructor
public class ResultadoVotacaoController {

    private final ResultadoVotacaoService resultadoVotacaoService;

    @GetMapping
    @Operation(
            summary = "Contabilizar votos e retornar resultado da pauta",
            description = """
                    Retorna a contagem de votos SIM e NAO da pauta.
                    O campo `resultado` assume SIM quando há maioria favorável, NAO quando há maioria contrária,
                    ou EMPATE quando votosSim e votosNao são iguais.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Resultado calculado com sucesso",
            content = @Content(schema = @Schema(implementation = ResultadoVotacaoResponse.class))
    )
    @ApiResponse(responseCode = "404", description = "Pauta não encontrada")
    public ResultadoVotacaoResponse consultar(
            @Parameter(description = "Identificador da pauta")
            @PathVariable Long pautaId
    ) {
        return resultadoVotacaoService.calcular(pautaId);
    }
}
