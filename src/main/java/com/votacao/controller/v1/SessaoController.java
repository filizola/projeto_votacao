package com.votacao.controller.v1;

import com.votacao.dto.AbrirSessaoRequest;
import com.votacao.dto.SessaoResponse;
import com.votacao.service.SessaoVotacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pautas/{pautaId}/sessoes")
@Tag(name = "Sessões de votação")
@RequiredArgsConstructor
public class SessaoController {

    private final SessaoVotacaoService sessaoVotacaoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Abrir sessão de votação em uma pauta")
    public SessaoResponse abrir(
            @PathVariable Long pautaId,
            @Valid @RequestBody(required = false) AbrirSessaoRequest request
    ) {
        return sessaoVotacaoService.abrir(pautaId, request);
    }

    @GetMapping("/atual")
    @Operation(summary = "Consultar a sessão mais recente da pauta")
    public SessaoResponse buscarAtual(@PathVariable Long pautaId) {
        return sessaoVotacaoService.buscarUltima(pautaId);
    }
}
