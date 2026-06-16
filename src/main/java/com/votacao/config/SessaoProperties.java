package com.votacao.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "votacao.sessao")
public record SessaoProperties(int duracaoPadraoMinutos) {
}
