package com.frota.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frota.api.model.Veiculo;
import com.frota.api.repository.VeiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VeiculoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VeiculoRepository repository;

    @BeforeEach
    void limparBanco() {
        repository.deleteAll();
    }

    private String json(Veiculo v) throws Exception {
        return objectMapper.writeValueAsString(v);
    }

    private Veiculo salvar(String placa, String marca, String modelo, int ano) {
        return repository.save(new Veiculo(placa, marca, modelo, ano));
    }

    @Test
    void deveCriarVeiculo() throws Exception {
        Veiculo v = new Veiculo("abc-1234", "Fiat", "Uno", 2015);

        mockMvc.perform(post("/veiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(v)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.placa", is("ABC1234")))
                .andExpect(jsonPath("$.marca", is("Fiat")));
    }

    @Test
    void deveListarVeiculos() throws Exception {
        salvar("AAA1111", "Fiat", "Uno", 2015);
        salvar("BBB2222", "VW", "Gol", 2018);

        mockMvc.perform(get("/veiculos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void deveBuscarPorId() throws Exception {
        Veiculo salvo = salvar("AAA1111", "Fiat", "Uno", 2015);

        mockMvc.perform(get("/veiculos/" + salvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modelo", is("Uno")));
    }

    @Test
    void deveRetornar404QuandoNaoExistir() throws Exception {
        mockMvc.perform(get("/veiculos/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveAtualizarVeiculo() throws Exception {
        Veiculo salvo = salvar("AAA1111", "Fiat", "Uno", 2015);
        Veiculo novo = new Veiculo("AAA1111", "Fiat", "Argo", 2022);

        mockMvc.perform(put("/veiculos/" + salvo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(novo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modelo", is("Argo")))
                .andExpect(jsonPath("$.ano", is(2022)));
    }

    @Test
    void deveRetornar404AoAtualizarInexistente() throws Exception {
        Veiculo novo = new Veiculo("AAA1111", "Fiat", "Argo", 2022);

        mockMvc.perform(put("/veiculos/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(novo)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveDeletarVeiculo() throws Exception {
        Veiculo salvo = salvar("AAA1111", "Fiat", "Uno", 2015);

        mockMvc.perform(delete("/veiculos/" + salvo.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/veiculos/" + salvo.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornar404AoDeletarInexistente() throws Exception {
        mockMvc.perform(delete("/veiculos/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRejeitarPlacaDuplicada() throws Exception {
        salvar("AAA1111", "Fiat", "Uno", 2015);
        Veiculo duplicado = new Veiculo("aaa-1111", "VW", "Gol", 2018);

        mockMvc.perform(post("/veiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(duplicado)))
                .andExpect(status().isConflict());
    }

    @Test
    void deveRejeitarDadosInvalidos() throws Exception {
        Veiculo invalido = new Veiculo("123", "", "Uno", 1800);

        mockMvc.perform(post("/veiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(invalido)))
                .andExpect(status().isBadRequest());
    }
}
