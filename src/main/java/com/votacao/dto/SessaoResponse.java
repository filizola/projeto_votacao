package com.votacao.dto;

import com.votacao.domain.SessaoVotacao;
import java.time.Instant;

public record SessaoResponse(
        Long id,
        Long pautaId,
        Instant inicio,
        Instant fim,
        boolean aberta
) {
    public static SessaoResponse from(SessaoVotacao sessao, Instant referencia) {
        return new SessaoResponse(
                sessao.getId(),
                sessao.getPauta().getId(),
                sessao.getInicio(),
                sessao.getFim(),
                sessao.isAberta(referencia)
        );
    }
}
