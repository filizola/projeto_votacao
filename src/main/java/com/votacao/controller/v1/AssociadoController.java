package com.votacao.controller.v1;

import com.votacao.dto.AutorizacaoVotoResponse;
import com.votacao.service.AutorizacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/associados")
@Tag(name = "Associados")
@RequiredArgsConstructor
public class AssociadoController {

    private final AutorizacaoService autorizacaoService;

    @GetMapping("/{cpf}/autorizacao")
    @Operation(summary = "Consultar se associado pode votar (integração fake com CPF)")
    public AutorizacaoVotoResponse consultarAutorizacao(@PathVariable String cpf) {
        return autorizacaoService.consultarAutorizacao(cpf);
    }
}
