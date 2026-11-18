package br.com.fiap.varresp.bo;

import br.com.fiap.varresp.dao.PrevisaoSemanalDao;
import br.com.fiap.varresp.dto.PrioridadeResponse;
import br.com.fiap.varresp.exception.RequisicaoInvalidaException;
import br.com.fiap.varresp.model.PrevisaoSemanal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PrioridadeBo {

    private static final int MAXIMO_DE_VAGAS = 96;

    private final PrevisaoSemanalDao previsaoDao;

    public PrioridadeBo(PrevisaoSemanalDao previsaoDao) {
        this.previsaoDao = previsaoDao;
    }

    @Transactional(readOnly = true)
    public List<PrioridadeResponse> priorizar(LocalDate semana, int vagas) {
        Semanas.exigirSegundaFeira(semana);
        if (vagas < 1 || vagas > MAXIMO_DE_VAGAS) {
            throw new RequisicaoInvalidaException("As vagas devem estar entre 1 e " + MAXIMO_DE_VAGAS);
        }
        List<PrevisaoSemanal> previsoes = previsaoDao.buscarPrioridades(semana, PageRequest.of(0, vagas));
        List<PrioridadeResponse> fila = new ArrayList<>();
        for (int i = 0; i < previsoes.size(); i++) {
            PrevisaoSemanal previsao = previsoes.get(i);
            fila.add(new PrioridadeResponse(
                    i + 1,
                    previsao.getDistrito().getId(),
                    previsao.getDistrito().getNome(),
                    previsao.getProbAlta(),
                    previsao.getVolumePrevisto()));
        }
        return fila;
    }
}
