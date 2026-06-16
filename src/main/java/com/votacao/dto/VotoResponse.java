package com.votacao.dto;

import com.votacao.domain.OpcaoVoto;
import com.votacao.domain.Voto;
import java.time.Instant;

public record VotoResponse(
        Long id,
        Long pautaId,
        String associadoId,
        OpcaoVoto voto,
        Instant registradoEm
) {
    public static VotoResponse from(Voto voto) {
        return new VotoResponse(
                voto.getId(),
                voto.getPauta().getId(),
                voto.getAssociadoId(),
                voto.getOpcao(),
                voto.getRegistradoEm()
        );
    }
}
