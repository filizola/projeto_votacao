package com.votacao.client;

import com.votacao.dto.StatusAutorizacaoVoto;

public sealed interface ResultadoValidacaoCpf permits ResultadoValidacaoCpf.CpfInvalido, ResultadoValidacaoCpf.CpfAutorizado {

    record CpfInvalido() implements ResultadoValidacaoCpf {
    }

    record CpfAutorizado(StatusAutorizacaoVoto status) implements ResultadoValidacaoCpf {
    }
}
