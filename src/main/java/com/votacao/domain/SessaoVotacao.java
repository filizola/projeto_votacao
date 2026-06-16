package com.votacao.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sessoes_votacao")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SessaoVotacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pauta_id", nullable = false)
    private Pauta pauta;

    @Column(nullable = false)
    private Instant inicio;

    @Column(nullable = false)
    private Instant fim;

    public SessaoVotacao(Pauta pauta, Instant inicio, Instant fim) {
        this.pauta = pauta;
        this.inicio = inicio;
        this.fim = fim;
    }

    public boolean isAberta(Instant referencia) {
        return !referencia.isBefore(inicio) && referencia.isBefore(fim);
    }
}
