package com.votacao.repository;

import com.votacao.domain.SessaoVotacao;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessaoVotacaoRepository extends JpaRepository<SessaoVotacao, Long> {

    Optional<SessaoVotacao> findTopByPautaIdOrderByInicioDesc(Long pautaId);
}
