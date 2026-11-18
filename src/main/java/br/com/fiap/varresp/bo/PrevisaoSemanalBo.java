package br.com.fiap.varresp.bo;

import br.com.fiap.varresp.dao.DistritoDao;
import br.com.fiap.varresp.dao.PrevisaoSemanalDao;
import br.com.fiap.varresp.dto.PrevisaoAtualizacao;
import br.com.fiap.varresp.dto.PrevisaoRequest;
import br.com.fiap.varresp.dto.PrevisaoResponse;
import br.com.fiap.varresp.exception.ConflitoException;
import br.com.fiap.varresp.exception.RecursoNaoEncontradoException;
import br.com.fiap.varresp.model.Distrito;
import br.com.fiap.varresp.model.DistritoSemanaId;
import br.com.fiap.varresp.model.PrevisaoSemanal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PrevisaoSemanalBo {

    private static final Sort ORDEM = Sort.by("id.semana", "id.idDistrito");

    private final PrevisaoSemanalDao previsaoDao;
    private final DistritoDao distritoDao;

    public PrevisaoSemanalBo(PrevisaoSemanalDao previsaoDao, DistritoDao distritoDao) {
        this.previsaoDao = previsaoDao;
        this.distritoDao = distritoDao;
    }

    @Transactional(readOnly = true)
    public List<PrevisaoResponse> listar(LocalDate semana) {
        List<PrevisaoSemanal> previsoes = semana == null
                ? previsaoDao.findAll(ORDEM)
                : previsaoDao.findByIdSemana(semana, ORDEM);
        return previsoes.stream().map(PrevisaoSemanalBo::paraResposta).toList();
    }

    @Transactional(readOnly = true)
    public PrevisaoResponse buscar(Long distritoId, LocalDate semana) {
        return paraResposta(obter(distritoId, semana));
    }

    @Transactional
    public PrevisaoResponse criar(PrevisaoRequest request) {
        Semanas.exigirSegundaFeira(request.semana());
        Distrito distrito = obterDistrito(request.distritoId());
        DistritoSemanaId id = new DistritoSemanaId(distrito.getId(), request.semana());
        if (previsaoDao.existsById(id)) {
            throw new ConflitoException("Já existe previsão para o distrito e a semana informados");
        }
        PrevisaoSemanal previsao = new PrevisaoSemanal();
        previsao.setId(id);
        previsao.setDistrito(distrito);
        previsao.setVolumePrevisto(request.volumePrevisto());
        previsao.setProbAlta(request.probAlta());
        previsao.setLimiteAlta(request.limiteAlta());
        return paraResposta(previsaoDao.save(previsao));
    }

    @Transactional
    public PrevisaoResponse atualizar(Long distritoId, LocalDate semana, PrevisaoAtualizacao request) {
        PrevisaoSemanal previsao = obter(distritoId, semana);
        previsao.setVolumePrevisto(request.volumePrevisto());
        previsao.setProbAlta(request.probAlta());
        previsao.setLimiteAlta(request.limiteAlta());
        return paraResposta(previsaoDao.save(previsao));
    }

    @Transactional
    public void remover(Long distritoId, LocalDate semana) {
        previsaoDao.delete(obter(distritoId, semana));
    }

    private PrevisaoSemanal obter(Long distritoId, LocalDate semana) {
        return previsaoDao.findById(new DistritoSemanaId(distritoId, semana))
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Previsão do distrito " + distritoId + " na semana " + semana + " não encontrada"));
    }

    private Distrito obterDistrito(Long id) {
        return distritoDao.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Distrito " + id + " não encontrado"));
    }

    private static PrevisaoResponse paraResposta(PrevisaoSemanal previsao) {
        Distrito distrito = previsao.getDistrito();
        return new PrevisaoResponse(
                distrito.getId(),
                distrito.getNome(),
                previsao.getId().getSemana(),
                previsao.getVolumePrevisto(),
                previsao.getProbAlta(),
                previsao.getLimiteAlta());
    }
}
