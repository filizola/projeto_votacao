package com.votacao.client;

import com.votacao.client.ResultadoValidacaoCpf.CpfAutorizado;
import com.votacao.client.ResultadoValidacaoCpf.CpfInvalido;
import com.votacao.dto.StatusAutorizacaoVoto;
import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ValidadorCpfFakeClient implements ValidadorCpfClient {

    @Override
    public ResultadoValidacaoCpf validar(String cpf) {
        log.debug("Validando CPF via client fake: {}", cpf);

        if (ThreadLocalRandom.current().nextBoolean()) {
            return new CpfInvalido();
        }

        StatusAutorizacaoVoto status = ThreadLocalRandom.current().nextBoolean()
                ? StatusAutorizacaoVoto.ABLE_TO_VOTE
                : StatusAutorizacaoVoto.UNABLE_TO_VOTE;

        return new CpfAutorizado(status);
    }
}
