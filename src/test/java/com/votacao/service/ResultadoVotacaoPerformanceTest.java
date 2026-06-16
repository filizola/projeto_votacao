package com.votacao.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.votacao.domain.OpcaoVoto;
import com.votacao.domain.Pauta;
import com.votacao.domain.ResultadoPauta;
import com.votacao.domain.Voto;
import com.votacao.dto.AbrirSessaoRequest;
import com.votacao.dto.CriarPautaRequest;
import com.votacao.repository.PautaRepository;
import com.votacao.repository.VotoRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ResultadoVotacaoPerformanceTest {

    private static final int TOTAL_VOTOS = 10_000;

    @Autowired
    private PautaService pautaService;

    @Autowired
    private SessaoVotacaoService sessaoVotacaoService;

    @Autowired
    private ResultadoVotacaoService resultadoVotacaoService;

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private VotoRepository votoRepository;

    @Test
    void deveContabilizarGrandeVolumeDeVotosDeFormaEficiente() {
        var pautaResponse = pautaService.cadastrar(new CriarPautaRequest("Performance", null));
        sessaoVotacaoService.abrir(pautaResponse.id(), new AbrirSessaoRequest(60));

        Pauta pauta = pautaRepository.findById(pautaResponse.id()).orElseThrow();
        List<Voto> votos = new ArrayList<>(TOTAL_VOTOS);
        Instant registradoEm = Instant.parse("2026-06-14T12:00:00Z");

        for (int i = 0; i < TOTAL_VOTOS; i++) {
            votos.add(new Voto(
                    pauta,
                    "associado-" + i,
                    i % 2 == 0 ? OpcaoVoto.SIM : OpcaoVoto.NAO,
                    registradoEm
            ));
        }

        votoRepository.saveAll(votos);

        long inicio = System.currentTimeMillis();
        var resultado = resultadoVotacaoService.calcular(pautaResponse.id());
        long duracaoMs = System.currentTimeMillis() - inicio;

        assertThat(resultado.totalVotos()).isEqualTo(TOTAL_VOTOS);
        assertThat(resultado.votosSim()).isEqualTo(5_000);
        assertThat(resultado.votosNao()).isEqualTo(5_000);
        assertThat(resultado.resultado()).isEqualTo(ResultadoPauta.EMPATE);
        assertThat(duracaoMs).isLessThan(2_000);
    }
}
