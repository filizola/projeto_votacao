package com.votacao.exception;

public sealed class VotacaoException extends RuntimeException
        permits RecursoNaoEncontradoException, RegraNegocioException, AssociadoNaoAutorizadoException {

    protected VotacaoException(String message) {
        super(message);
    }
}
