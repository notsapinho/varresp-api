package br.com.fiap.varresp;

import static org.hamcrest.Matchers.endsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class VarrespApiTest {

    private static final String SEGUNDA = "2025-03-03";
    private static final String OUTRA_SEGUNDA = "2025-03-10";
    private static final String TERCA = "2025-03-04";

    @Autowired
    private MockMvc mvc;

    private long criarDistrito(String nome) throws Exception {
        String corpo = mvc.perform(post("/api/distritos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"" + nome + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        return ((Number) JsonPath.read(corpo, "$.id")).longValue();
    }

    private void criarPrevisao(long distritoId, String semana, String volume, String probabilidade) throws Exception {
        mvc.perform(post("/api/previsoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"distritoId\":" + distritoId + ",\"semana\":\"" + semana + "\",\"volumePrevisto\":"
                                + volume + ",\"probAlta\":" + probabilidade + ",\"limiteAlta\":7}"))
                .andExpect(status().isCreated());
    }

    @Test
    void saudeResponde() throws Exception {
        mvc.perform(get("/api/saude"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"));
    }

    @Test
    void crudDeDistritoComIdsSequenciais() throws Exception {
        long primeiro = criarDistrito("lapa");
        long segundo = criarDistrito("sé");
        assertEquals(primeiro + 1, segundo);

        mvc.perform(get("/api/distritos/" + primeiro))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("LAPA"));

        mvc.perform(put("/api/distritos/" + primeiro)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Pinheiros\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("PINHEIROS"));

        mvc.perform(get("/api/distritos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + primeiro + ")].nome").value("PINHEIROS"));

        mvc.perform(delete("/api/distritos/" + primeiro)).andExpect(status().isNoContent());
        mvc.perform(get("/api/distritos/" + primeiro)).andExpect(status().isNotFound());
    }

    @Test
    void rejeitaDistritoDuplicado() throws Exception {
        criarDistrito("MOOCA");
        mvc.perform(post("/api/distritos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"mooca\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
        mvc.perform(put("/api/distritos/" + criarDistrito("PENHA"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Mooca\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void rejeitaNomeEmBrancoOuMuitoLongo() throws Exception {
        mvc.perform(post("/api/distritos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"  \"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.nome").exists());
        mvc.perform(post("/api/distritos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"" + "A".repeat(81) + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.nome").exists());
    }

    @Test
    void rejeitaCorpoMalformado() throws Exception {
        mvc.perform(post("/api/distritos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{nome"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void retorna404ParaRecursoEParaRotaInexistentes() throws Exception {
        mvc.perform(get("/api/distritos/999999")).andExpect(status().isNotFound());
        mvc.perform(get("/api/rota-que-nao-existe")).andExpect(status().isNotFound());
    }

    @Test
    void retorna405ParaMetodoNaoSuportado() throws Exception {
        mvc.perform(delete("/api/distritos")).andExpect(status().isMethodNotAllowed());
    }

    @Test
    void crudDeDemandaComChaveCompostaPorDistritoESemana() throws Exception {
        long distritoId = criarDistrito("SE");
        String corpo = "{\"distritoId\":" + distritoId + ",\"semana\":\"" + SEGUNDA + "\",\"pedidos\":12}";
        String rota = "/api/demandas/" + distritoId + "/" + SEGUNDA;

        mvc.perform(post("/api/demandas").contentType(MediaType.APPLICATION_JSON).content(corpo))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith(rota)))
                .andExpect(jsonPath("$.pedidos").value(12))
                .andExpect(jsonPath("$.distrito").value("SE"));

        mvc.perform(post("/api/demandas").contentType(MediaType.APPLICATION_JSON).content(corpo))
                .andExpect(status().isConflict());

        mvc.perform(get(rota))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.semana").value(SEGUNDA));

        mvc.perform(put(rota).contentType(MediaType.APPLICATION_JSON).content("{\"pedidos\":30}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pedidos").value(30));

        mvc.perform(get("/api/demandas").param("distritoId", String.valueOf(distritoId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        mvc.perform(delete(rota)).andExpect(status().isNoContent());
        mvc.perform(get(rota)).andExpect(status().isNotFound());
        mvc.perform(delete(rota)).andExpect(status().isNotFound());
    }

    @Test
    void exigeSemanaIniciadaNaSegundaFeira() throws Exception {
        long distritoId = criarDistrito("PENHA");
        mvc.perform(post("/api/demandas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"distritoId\":" + distritoId + ",\"semana\":\"" + TERCA + "\",\"pedidos\":5}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejeitaDemandaDeDistritoInexistenteOuComPedidosNegativos() throws Exception {
        mvc.perform(post("/api/demandas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"distritoId\":999999,\"semana\":\"" + SEGUNDA + "\",\"pedidos\":5}"))
                .andExpect(status().isNotFound());
        long distritoId = criarDistrito("ITAQUERA");
        mvc.perform(post("/api/demandas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"distritoId\":" + distritoId + ",\"semana\":\"" + SEGUNDA + "\",\"pedidos\":-1}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.pedidos").exists());
    }

    @Test
    void crudDePrevisaoEValidacaoDaProbabilidade() throws Exception {
        long distritoId = criarDistrito("BUTANTA");
        String rota = "/api/previsoes/" + distritoId + "/" + SEGUNDA;
        criarPrevisao(distritoId, SEGUNDA, "13.680", "0.954269");

        mvc.perform(get(rota))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limiteAlta").value(7))
                .andExpect(jsonPath("$.probAlta").value(0.954269));

        mvc.perform(put(rota)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"volumePrevisto\":20.5,\"probAlta\":0.5,\"limiteAlta\":9}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limiteAlta").value(9));

        mvc.perform(post("/api/previsoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"distritoId\":" + distritoId + ",\"semana\":\"" + OUTRA_SEGUNDA
                                + "\",\"volumePrevisto\":10,\"probAlta\":1.5,\"limiteAlta\":7}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.probAlta").exists());

        mvc.perform(post("/api/previsoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"distritoId\":" + distritoId + ",\"semana\":\"" + OUTRA_SEGUNDA
                                + "\",\"volumePrevisto\":10,\"probAlta\":0.5,\"limiteAlta\":0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.limiteAlta").exists());

        mvc.perform(delete(rota)).andExpect(status().isNoContent());
        mvc.perform(get(rota)).andExpect(status().isNotFound());
    }

    @Test
    void listaPrevisoesFiltrandoPorSemana() throws Exception {
        long a = criarDistrito("DISTRITO A");
        criarPrevisao(a, SEGUNDA, "10.0", "0.30");
        criarPrevisao(a, OUTRA_SEGUNDA, "11.0", "0.40");

        mvc.perform(get("/api/previsoes")).andExpect(jsonPath("$.length()").value(2));
        mvc.perform(get("/api/previsoes").param("semana", OUTRA_SEGUNDA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].semana").value(OUTRA_SEGUNDA));
    }

    @Test
    void priorizaPelaProbabilidadeDeAlta() throws Exception {
        long a = criarDistrito("DISTRITO A");
        long b = criarDistrito("DISTRITO B");
        long c = criarDistrito("DISTRITO C");
        long d = criarDistrito("DISTRITO D");
        criarPrevisao(a, SEGUNDA, "10.5", "0.30");
        criarPrevisao(b, SEGUNDA, "20.0", "0.90");
        criarPrevisao(c, SEGUNDA, "15.0", "0.60");
        criarPrevisao(d, SEGUNDA, "99.0", "0.60");
        criarPrevisao(a, OUTRA_SEGUNDA, "50.0", "0.99");

        mvc.perform(get("/api/prioridades").param("semana", SEGUNDA).param("vagas", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].posicao").value(1))
                .andExpect(jsonPath("$[0].distrito").value("DISTRITO B"))
                .andExpect(jsonPath("$[1].distrito").value("DISTRITO D"))
                .andExpect(jsonPath("$[2].distrito").value("DISTRITO C"));
    }

    @Test
    void prioridadeUsaTresVagasPorPadraoEValidaParametros() throws Exception {
        long a = criarDistrito("DISTRITO E");
        criarPrevisao(a, SEGUNDA, "10.0", "0.50");

        mvc.perform(get("/api/prioridades").param("semana", SEGUNDA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        mvc.perform(get("/api/prioridades").param("semana", SEGUNDA).param("vagas", "0"))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/api/prioridades").param("semana", TERCA))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/api/prioridades").param("semana", "ontem"))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/api/prioridades"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void naoRemoveDistritoQueTemPrevisoesOuDemandas() throws Exception {
        long comPrevisao = criarDistrito("DISTRITO F");
        criarPrevisao(comPrevisao, SEGUNDA, "10.0", "0.50");
        mvc.perform(delete("/api/distritos/" + comPrevisao)).andExpect(status().isConflict());

        long comDemanda = criarDistrito("DISTRITO G");
        mvc.perform(post("/api/demandas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"distritoId\":" + comDemanda + ",\"semana\":\"" + SEGUNDA + "\",\"pedidos\":3}"))
                .andExpect(status().isCreated());
        mvc.perform(delete("/api/distritos/" + comDemanda)).andExpect(status().isConflict());
    }
}
