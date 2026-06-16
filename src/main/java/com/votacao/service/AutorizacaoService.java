package com.votacao.service;

import com.votacao.client.ResultadoValidacaoCpf;
import com.votacao.client.ResultadoValidacaoCpf.CpfAutorizado;
import com.votacao.client.ResultadoValidacaoCpf.CpfInvalido;
import com.votacao.client.ValidadorCpfClient;
import com.votacao.dto.AutorizacaoVotoResponse;
import com.votacao.dto.StatusAutorizacaoVoto;
import com.votacao.exception.AssociadoNaoAutorizadoException;
import com.votacao.exception.RecursoNaoEncontradoException;
import java.util.Optional;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutorizacaoService {

    private final ValidadorCpfClient validadorCpfClient;

    public AutorizacaoVotoResponse consultarAutorizacao(String cpf) {
        ResultadoValidacaoCpf resultado = validadorCpfClient.validar(cpf);
        validarResultado(cpf, resultado);
        return extrairResposta(resultado);
    }

    public void validarCpfParaVoto(String cpf) {
        Optional.ofNullable(cpf)
                .filter(Predicate.not(String::isBlank))
                .ifPresent(cpfInformado -> validarResultado(cpfInformado, validadorCpfClient.validar(cpfInformado)));
    }

    private AutorizacaoVotoResponse extrairResposta(ResultadoValidacaoCpf resultado) {
        return switch (resultado) {
            case CpfAutorizado(var status) -> new AutorizacaoVotoResponse(status);
            case CpfInvalido ignored -> throw new IllegalStateException("CPF inválido após validação bem-sucedida");
        };
    }

    private void validarResultado(String cpf, ResultadoValidacaoCpf resultado) {
        switch (resultado) {
            case CpfInvalido ignored -> throw new RecursoNaoEncontradoException("CPF não encontrado: " + cpf);
            case CpfAutorizado(var status) when status == StatusAutorizacaoVoto.UNABLE_TO_VOTE ->
                    throw new AssociadoNaoAutorizadoException("Associado não autorizado a votar");
            case CpfAutorizado ignored -> {
            }
        }
    }
}
