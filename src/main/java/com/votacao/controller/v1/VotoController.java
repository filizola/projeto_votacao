package com.votacao.controller.v1;

import com.votacao.dto.RegistrarVotoRequest;
import com.votacao.dto.VotoResponse;
import com.votacao.service.VotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pautas/{pautaId}/votos")
@Tag(name = "Votos")
@RequiredArgsConstructor
public class VotoController {

    private final VotoService votoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar voto de um associado em uma pauta")
    public VotoResponse registrar(
            @PathVariable Long pautaId,
            @Valid @RequestBody RegistrarVotoRequest request
    ) {
        return votoService.registrar(pautaId, request);
    }
}
