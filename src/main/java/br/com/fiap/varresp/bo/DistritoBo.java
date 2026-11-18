package br.com.fiap.varresp.bo;

import br.com.fiap.varresp.dao.DemandaSemanalDao;
import br.com.fiap.varresp.dao.DistritoDao;
import br.com.fiap.varresp.dao.PrevisaoSemanalDao;
import br.com.fiap.varresp.dto.DistritoRequest;
import br.com.fiap.varresp.dto.DistritoResponse;
import br.com.fiap.varresp.exception.ConflitoException;
import br.com.fiap.varresp.exception.RecursoNaoEncontradoException;
import br.com.fiap.varresp.model.Distrito;
import java.util.List;
import java.util.Locale;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DistritoBo {

    private static final long MAIOR_ID = 999L;

    private final DistritoDao distritoDao;
    private final DemandaSemanalDao demandaDao;
    private final PrevisaoSemanalDao previsaoDao;

    public DistritoBo(DistritoDao distritoDao, DemandaSemanalDao demandaDao, PrevisaoSemanalDao previsaoDao) {
        this.distritoDao = distritoDao;
        this.demandaDao = demandaDao;
        this.previsaoDao = previsaoDao;
    }

    @Transactional(readOnly = true)
    public List<DistritoResponse> listar() {
        return distritoDao.findAll(Sort.by("nome")).stream().map(DistritoBo::paraResposta).toList();
    }

    @Transactional(readOnly = true)
    public DistritoResponse buscar(Long id) {
        return paraResposta(obter(id));
    }

    @Transactional
    public DistritoResponse criar(DistritoRequest request) {
        String nome = normalizar(request.nome());
        if (distritoDao.existsByNome(nome)) {
            throw new ConflitoException("Já existe um distrito com o nome " + nome);
        }
        long proximoId = distritoDao.maiorId() + 1;
        if (proximoId > MAIOR_ID) {
            throw new ConflitoException("O limite de " + MAIOR_ID + " distritos foi atingido");
        }
        Distrito distrito = new Distrito();
        distrito.setId(proximoId);
        distrito.setNome(nome);
        return paraResposta(distritoDao.save(distrito));
    }

    @Transactional
    public DistritoResponse atualizar(Long id, DistritoRequest request) {
        Distrito distrito = obter(id);
        String nome = normalizar(request.nome());
        if (distritoDao.existsByNomeAndIdNot(nome, id)) {
            throw new ConflitoException("Já existe um distrito com o nome " + nome);
        }
        distrito.setNome(nome);
        return paraResposta(distritoDao.save(distrito));
    }

    @Transactional
    public void remover(Long id) {
        Distrito distrito = obter(id);
        if (demandaDao.existsByDistritoId(id) || previsaoDao.existsByDistritoId(id)) {
            throw new ConflitoException("O distrito possui demandas ou previsões e não pode ser removido");
        }
        distritoDao.delete(distrito);
    }

    private Distrito obter(Long id) {
        return distritoDao.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Distrito " + id + " não encontrado"));
    }

    private static String normalizar(String nome) {
        return nome.trim().toUpperCase(Locale.ROOT);
    }

    private static DistritoResponse paraResposta(Distrito distrito) {
        return new DistritoResponse(distrito.getId(), distrito.getNome());
    }
}
