package com.votacao.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.votacao.client.ResultadoValidacaoCpf.CpfAutorizado;
import com.votacao.client.ResultadoValidacaoCpf.CpfInvalido;
import com.votacao.client.ValidadorCpfClient;
import com.votacao.dto.StatusAutorizacaoVoto;
import com.votacao.exception.AssociadoNaoAutorizadoException;
import com.votacao.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.Test;

class AutorizacaoServiceTest {

    @Test
    void deveRetornar404QuandoCpfInvalido() {
        ValidadorCpfClient client = cpf -> new CpfInvalido();
        var service = new AutorizacaoService(client);

        assertThatThrownBy(() -> service.consultarAutorizacao("00000000000"))
                .isInstanceOf(RecursoNaoEncontradoException.class);
    }

    @Test
    void deveRetornarAbleToVoteQuandoCpfValido() {
        ValidadorCpfClient client = cpf -> new CpfAutorizado(StatusAutorizacaoVoto.ABLE_TO_VOTE);
        var service = new AutorizacaoService(client);

        var response = service.consultarAutorizacao("39053344705");

        assertThat(response.status()).isEqualTo(StatusAutorizacaoVoto.ABLE_TO_VOTE);
    }

    @Test
    void deveLancarExcecaoQuandoCpfValidoMasNaoAutorizado() {
        ValidadorCpfClient client = cpf -> new CpfAutorizado(StatusAutorizacaoVoto.UNABLE_TO_VOTE);
        var service = new AutorizacaoService(client);

        assertThatThrownBy(() -> service.consultarAutorizacao("39053344705"))
                .isInstanceOf(AssociadoNaoAutorizadoException.class);
    }

    @Test
    void deveIgnorarValidacaoQuandoCpfNaoInformado() {
        ValidadorCpfClient client = cpf -> {
            throw new IllegalStateException("Client não deveria ser chamado");
        };
        var service = new AutorizacaoService(client);

        service.validarCpfParaVoto(null);
        service.validarCpfParaVoto("   ");
    }
}
