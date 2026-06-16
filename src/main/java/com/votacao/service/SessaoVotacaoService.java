package com.votacao.service;

import com.votacao.config.SessaoProperties;
import com.votacao.domain.Pauta;
import com.votacao.domain.SessaoVotacao;
import com.votacao.dto.AbrirSessaoRequest;
import com.votacao.dto.SessaoResponse;
import com.votacao.exception.RecursoNaoEncontradoException;
import com.votacao.exception.RegraNegocioException;
import com.votacao.repository.SessaoVotacaoRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessaoVotacaoService {

    private final SessaoVotacaoRepository sessaoRepository;
    private final PautaService pautaService;
    private final SessaoProperties sessaoProperties;
    private final Clock clock;

    @Transactional
    public SessaoResponse abrir(Long pautaId, AbrirSessaoRequest request) {
        Pauta pauta = pautaService.buscarEntidade(pautaId);
        Instant agora = clock.instant();

        buscarUltimaSessao(pautaId)
                .filter(sessao -> sessao.isAberta(agora))
                .ifPresent(sessao -> {
                    throw new RegraNegocioException("Já existe uma sessão de votação aberta para esta pauta");
                });

        int duracaoMinutos = AbrirSessaoRequest.resolverDuracao(request, sessaoProperties.duracaoPadraoMinutos());
        Instant fim = agora.plus(duracaoMinutos, ChronoUnit.MINUTES);
        SessaoVotacao sessao = sessaoRepository.save(new SessaoVotacao(pauta, agora, fim));

        log.info("Sessão aberta: id={}, pautaId={}, duracaoMinutos={}", sessao.getId(), pautaId, duracaoMinutos);
        return SessaoResponse.from(sessao, agora);
    }

    @Transactional(readOnly = true)
    public SessaoResponse buscarUltima(Long pautaId) {
        pautaService.validarExistencia(pautaId);

        SessaoVotacao sessao = buscarUltimaSessao(pautaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Nenhuma sessão de votação encontrada para a pauta: " + pautaId));

        return SessaoResponse.from(sessao, clock.instant());
    }

    @Transactional(readOnly = true)
    public void validarSessaoAberta(Long pautaId) {
        Instant agora = clock.instant();
        SessaoVotacao sessao = buscarUltimaSessao(pautaId)
                .orElseThrow(() -> new RegraNegocioException("Não há sessão de votação aberta para esta pauta"));

        if (!sessao.isAberta(agora)) {
            throw new RegraNegocioException("A sessão de votação está encerrada");
        }
    }

    private Optional<SessaoVotacao> buscarUltimaSessao(Long pautaId) {
        return sessaoRepository.findTopByPautaIdOrderByInicioDesc(pautaId);
    }
}
