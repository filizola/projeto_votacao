package com.votacao.repository;

import com.votacao.domain.OpcaoVoto;
import com.votacao.domain.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VotoRepository extends JpaRepository<Voto, Long> {

    boolean existsByPautaIdAndAssociadoId(Long pautaId, String associadoId);

    @Query("""
            SELECT COUNT(v) FROM Voto v
            WHERE v.pauta.id = :pautaId AND v.opcao = :opcao
            """)
    long countByPautaIdAndOpcao(@Param("pautaId") Long pautaId, @Param("opcao") OpcaoVoto opcao);
}
