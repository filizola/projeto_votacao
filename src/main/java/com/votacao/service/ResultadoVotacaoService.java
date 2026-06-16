package com.votacao.service;

import com.votacao.domain.OpcaoVoto;
import com.votacao.domain.ResultadoPauta;
import com.votacao.dto.ResultadoVotacaoResponse;
import com.votacao.repository.VotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResultadoVotacaoService {

    private final VotoRepository votoRepository;
    private final PautaService pautaService;

    @Transactional(readOnly = true)
    public ResultadoVotacaoResponse calcular(Long pautaId) {
        pautaService.validarExistencia(pautaId);

        long votosSim = votoRepository.countByPautaIdAndOpcao(pautaId, OpcaoVoto.SIM);
        long votosNao = votoRepository.countByPautaIdAndOpcao(pautaId, OpcaoVoto.NAO);

        return new ResultadoVotacaoResponse(
                pautaId,
                votosSim + votosNao,
                votosSim,
                votosNao,
                ResultadoPauta.calcular(votosSim, votosNao)
        );
    }
}
