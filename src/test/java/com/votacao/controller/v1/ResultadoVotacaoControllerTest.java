package com.votacao.controller.v1;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.votacao.domain.OpcaoVoto;
import com.votacao.dto.AbrirSessaoRequest;
import com.votacao.dto.CriarPautaRequest;
import com.votacao.dto.RegistrarVotoRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ResultadoVotacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveRetornarResultadoComMaioriaSim() throws Exception {
        long pautaId = cadastrarPauta("Pauta resultado SIM");
        abrirSessao(pautaId);
        registrarVoto(pautaId, "associado-1", OpcaoVoto.SIM);
        registrarVoto(pautaId, "associado-2", OpcaoVoto.SIM);
        registrarVoto(pautaId, "associado-3", OpcaoVoto.NAO);

        mockMvc.perform(get("/api/v1/pautas/{pautaId}/resultado", pautaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pautaId").value(pautaId))
                .andExpect(jsonPath("$.totalVotos").value(3))
                .andExpect(jsonPath("$.votosSim").value(2))
                .andExpect(jsonPath("$.votosNao").value(1))
                .andExpect(jsonPath("$.resultado").value("SIM"));
    }

    @Test
    void deveRetornarEmpateQuandoVotosSaoIguais() throws Exception {
        long pautaId = cadastrarPauta("Pauta empate");
        abrirSessao(pautaId);
        registrarVoto(pautaId, "associado-1", OpcaoVoto.SIM);
        registrarVoto(pautaId, "associado-2", OpcaoVoto.NAO);

        mockMvc.perform(get("/api/v1/pautas/{pautaId}/resultado", pautaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalVotos").value(2))
                .andExpect(jsonPath("$.votosSim").value(1))
                .andExpect(jsonPath("$.votosNao").value(1))
                .andExpect(jsonPath("$.resultado").value("EMPATE"));
    }

    @Test
    void deveRetornar404QuandoPautaNaoExiste() throws Exception {
        mockMvc.perform(get("/api/v1/pautas/{pautaId}/resultado", 999))
                .andExpect(status().isNotFound());
    }

    private long cadastrarPauta(String titulo) throws Exception {
        var request = new CriarPautaRequest(titulo, null);
        String response = mockMvc.perform(post("/api/v1/pautas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("id").asLong();
    }

    private void abrirSessao(long pautaId) throws Exception {
        mockMvc.perform(post("/api/v1/pautas/{pautaId}/sessoes", pautaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AbrirSessaoRequest(5))))
                .andExpect(status().isCreated());
    }

    private void registrarVoto(long pautaId, String associadoId, OpcaoVoto voto) throws Exception {
        var request = new RegistrarVotoRequest(associadoId, voto, null);
        mockMvc.perform(post("/api/v1/pautas/{pautaId}/votos", pautaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }
}
