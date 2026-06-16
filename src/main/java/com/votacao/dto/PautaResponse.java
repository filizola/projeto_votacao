package com.votacao.dto;

import com.votacao.domain.Pauta;
import java.time.Instant;

public record PautaResponse(
        Long id,
        String titulo,
        String descricao,
        Instant criadaEm
) {
    public static PautaResponse from(Pauta pauta) {
        return new PautaResponse(pauta.getId(), pauta.getTitulo(), pauta.getDescricao(), pauta.getCriadaEm());
    }
}
