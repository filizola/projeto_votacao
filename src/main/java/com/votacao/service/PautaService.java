package com.votacao.service;

import com.votacao.domain.Pauta;
import com.votacao.dto.CriarPautaRequest;
import com.votacao.dto.PautaResponse;
import com.votacao.exception.RecursoNaoEncontradoException;
import com.votacao.repository.PautaRepository;
import java.time.Clock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PautaService {

    private final PautaRepository pautaRepository;
    private final Clock clock;

    @Transactional
    public PautaResponse cadastrar(CriarPautaRequest request) {
        Pauta pauta = pautaRepository.save(criarEntidade(request));
        log.info("Pauta cadastrada: id={}, titulo={}", pauta.getId(), pauta.getTitulo());
        return PautaResponse.from(pauta);
    }

    @Transactional(readOnly = true)
    public PautaResponse buscarPorId(Long id) {
        return PautaResponse.from(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public Pauta buscarEntidade(Long id) {
        return pautaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pauta não encontrada: " + id));
    }

    @Transactional(readOnly = true)
    public void validarExistencia(Long id) {
        if (!pautaRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Pauta não encontrada: " + id);
        }
    }

    private Pauta criarEntidade(CriarPautaRequest request) {
        return new Pauta(request.titulo(), request.descricao(), clock.instant());
    }
}
