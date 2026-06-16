package com.votacao.domain;

import com.votacao.dto.PautaConstraints;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pautas")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = PautaConstraints.TITULO_MAX_LENGTH)
    private String titulo;

    @Column(length = PautaConstraints.DESCRICAO_MAX_LENGTH)
    private String descricao;

    @Column(nullable = false, updatable = false)
    private Instant criadaEm;

    public Pauta(String titulo, String descricao, Instant criadaEm) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.criadaEm = criadaEm;
    }
}
