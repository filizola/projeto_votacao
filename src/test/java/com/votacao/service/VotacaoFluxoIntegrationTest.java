package com.votacao.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.votacao.domain.OpcaoVoto;
import com.votacao.domain.ResultadoPauta;
import com.votacao.dto.AbrirSessaoRequest;
import com.votacao.dto.CriarPautaRequest;
import com.votacao.dto.RegistrarVotoRequest;
import com.votacao.dto.ResultadoVotacaoResponse;
import com.votacao.exception.RegraNegocioException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class VotacaoFluxoIntegrationTest {

    @Autowired
    private PautaService pautaService;

    @Autowired
    private SessaoVotacaoService sessaoVotacaoService;

    @Autowired
    private VotoService votoService;

    @Autowired
    private ResultadoVotacaoService resultadoVotacaoService;

    @Test
    void deveExecutarFluxoCompletoDeVotacao() {
        var pauta = pautaService.cadastrar(new CriarPautaRequest("Aprovação de orçamento", "Descrição"));
        sessaoVotacaoService.abrir(pauta.id(), new AbrirSessaoRequest(5));

        votoService.registrar(pauta.id(), new RegistrarVotoRequest("associado-1", OpcaoVoto.SIM, null));
        votoService.registrar(pauta.id(), new RegistrarVotoRequest("associado-2", OpcaoVoto.NAO, null));
        votoService.registrar(pauta.id(), new RegistrarVotoRequest("associado-3", OpcaoVoto.SIM, null));

        ResultadoVotacaoResponse resultado = resultadoVotacaoService.calcular(pauta.id());

        assertThat(resultado.totalVotos()).isEqualTo(3);
        assertThat(resultado.votosSim()).isEqualTo(2);
        assertThat(resultado.votosNao()).isEqualTo(1);
        assertThat(resultado.resultado()).isEqualTo(ResultadoPauta.SIM);
    }

    @Test
    void naoDevePermitirVotoDuplicado() {
        var pauta = pautaService.cadastrar(new CriarPautaRequest("Pauta duplicada", null));
        sessaoVotacaoService.abrir(pauta.id(), null);

        var request = new RegistrarVotoRequest("associado-1", OpcaoVoto.SIM, null);
        votoService.registrar(pauta.id(), request);

        assertThatThrownBy(() -> votoService.registrar(pauta.id(), request))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessageContaining("já votou");
    }

    @Test
    void deveUsarDuracaoPadraoDeUmMinutoQuandoNaoInformada() {
        var pauta = pautaService.cadastrar(new CriarPautaRequest("Pauta default", null));
        var sessao = sessaoVotacaoService.abrir(pauta.id(), null);

        assertThat(sessao.inicio()).isBefore(sessao.fim());
    }
}
