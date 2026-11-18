package br.com.fiap.varresp.bo;

import br.com.fiap.varresp.dao.DemandaSemanalDao;
import br.com.fiap.varresp.dao.DistritoDao;
import br.com.fiap.varresp.dto.DemandaAtualizacao;
import br.com.fiap.varresp.dto.DemandaRequest;
import br.com.fiap.varresp.dto.DemandaResponse;
import br.com.fiap.varresp.exception.ConflitoException;
import br.com.fiap.varresp.exception.RecursoNaoEncontradoException;
import br.com.fiap.varresp.model.DemandaSemanal;
import br.com.fiap.varresp.model.Distrito;
import br.com.fiap.varresp.model.DistritoSemanaId;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DemandaSemanalBo {

    private final DemandaSemanalDao demandaDao;
    private final DistritoDao distritoDao;

    public DemandaSemanalBo(DemandaSemanalDao demandaDao, DistritoDao distritoDao) {
        this.demandaDao = demandaDao;
        this.distritoDao = distritoDao;
    }

    @Transactional(readOnly = true)
    public List<DemandaResponse> listar(Long distritoId) {
        List<DemandaSemanal> demandas = distritoId == null
                ? demandaDao.findAll(Sort.by("id.semana", "id.idDistrito"))
                : demandaDao.findByDistritoIdOrderByIdSemanaAsc(distritoId);
        return demandas.stream().map(DemandaSemanalBo::paraResposta).toList();
    }

    @Transactional(readOnly = true)
    public DemandaResponse buscar(Long distritoId, LocalDate semana) {
        return paraResposta(obter(distritoId, semana));
    }

    @Transactional
    public DemandaResponse criar(DemandaRequest request) {
        Semanas.exigirSegundaFeira(request.semana());
        Distrito distrito = obterDistrito(request.distritoId());
        DistritoSemanaId id = new DistritoSemanaId(distrito.getId(), request.semana());
        if (demandaDao.existsById(id)) {
            throw new ConflitoException("Já existe demanda para o distrito e a semana informados");
        }
        DemandaSemanal demanda = new DemandaSemanal();
        demanda.setId(id);
        demanda.setDistrito(distrito);
        demanda.setPedidos(request.pedidos());
        return paraResposta(demandaDao.save(demanda));
    }

    @Transactional
    public DemandaResponse atualizar(Long distritoId, LocalDate semana, DemandaAtualizacao request) {
        DemandaSemanal demanda = obter(distritoId, semana);
        demanda.setPedidos(request.pedidos());
        return paraResposta(demandaDao.save(demanda));
    }

    @Transactional
    public void remover(Long distritoId, LocalDate semana) {
        demandaDao.delete(obter(distritoId, semana));
    }

    private DemandaSemanal obter(Long distritoId, LocalDate semana) {
        return demandaDao.findById(new DistritoSemanaId(distritoId, semana))
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Demanda do distrito " + distritoId + " na semana " + semana + " não encontrada"));
    }

    private Distrito obterDistrito(Long id) {
        return distritoDao.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Distrito " + id + " não encontrado"));
    }

    private static DemandaResponse paraResposta(DemandaSemanal demanda) {
        Distrito distrito = demanda.getDistrito();
        return new DemandaResponse(distrito.getId(), distrito.getNome(), demanda.getId().getSemana(), demanda.getPedidos());
    }
}
