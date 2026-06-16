package com.votacao.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ResultadoPautaTest {

    @Test
    void deveRetornarSimQuandoMaioriaFavoravel() {
        assertThat(ResultadoPauta.calcular(3, 1)).isEqualTo(ResultadoPauta.SIM);
    }

    @Test
    void deveRetornarNaoQuandoMaioriaContraria() {
        assertThat(ResultadoPauta.calcular(1, 4)).isEqualTo(ResultadoPauta.NAO);
    }

    @Test
    void deveRetornarEmpateQuandoVotosIguais() {
        assertThat(ResultadoPauta.calcular(2, 2)).isEqualTo(ResultadoPauta.EMPATE);
    }
}
