package com.votacao.service;

import com.votacao.domain.Pauta;
import com.votacao.domain.Voto;
import com.votacao.dto.RegistrarVotoRequest;
import com.votacao.dto.VotoResponse;
import com.votacao.exception.RegraNegocioException;
import com.votacao.repository.VotoRepository;
import java.time.Clock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VotoService {

    private final VotoRepository votoRepository;
    private final PautaService pautaService;
    private final SessaoVotacaoService sessaoVotacaoService;
    private final AutorizacaoService autorizacaoService;
    private final Clock clock;

    @Transactional
    public VotoResponse registrar(Long pautaId, RegistrarVotoRequest request) {
        sessaoVotacaoService.validarSessaoAberta(pautaId);
        autorizacaoService.validarCpfParaVoto(request.cpf());
        validarVotoUnico(pautaId, request.associadoId());

        Pauta pauta = pautaService.buscarEntidade(pautaId);
        Voto voto = votoRepository.save(new Voto(
                pauta,
                request.associadoId(),
                request.voto(),
                clock.instant()
        ));

        log.info("Voto registrado: pautaId={}, associadoId={}, voto={}",
                pautaId, request.associadoId(), request.voto());

        return VotoResponse.from(voto);
    }

    private void validarVotoUnico(Long pautaId, String associadoId) {
        if (votoRepository.existsByPautaIdAndAssociadoId(pautaId, associadoId)) {
            throw new RegraNegocioException("Associado já votou nesta pauta");
        }
    }
}
