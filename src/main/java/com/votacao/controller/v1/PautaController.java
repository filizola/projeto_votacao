package com.votacao.controller.v1;

import com.votacao.dto.CriarPautaRequest;
import com.votacao.dto.PautaResponse;
import com.votacao.service.PautaService;
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
@RequestMapping("/api/v1/pautas")
@Tag(name = "Pautas")
@RequiredArgsConstructor
public class PautaController {

    private final PautaService pautaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar uma nova pauta")
    public PautaResponse cadastrar(@Valid @RequestBody CriarPautaRequest request) {
        return pautaService.cadastrar(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar pauta por id")
    public PautaResponse buscar(@PathVariable Long id) {
        return pautaService.buscarPorId(id);
    }
}
