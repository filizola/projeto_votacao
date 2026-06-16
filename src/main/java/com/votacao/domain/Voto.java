package com.votacao.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "votos",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_voto_pauta_associado",
                columnNames = {"pauta_id", "associado_id"}
        ),
        indexes = {
                @Index(name = "idx_voto_pauta_opcao", columnList = "pauta_id, opcao")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pauta_id", nullable = false)
    private Pauta pauta;

    @Column(name = "associado_id", nullable = false)
    private String associadoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OpcaoVoto opcao;

    @Column(nullable = false, updatable = false)
    private Instant registradoEm;

    public Voto(Pauta pauta, String associadoId, OpcaoVoto opcao, Instant registradoEm) {
        this.pauta = pauta;
        this.associadoId = associadoId;
        this.opcao = opcao;
        this.registradoEm = registradoEm;
    }
}
