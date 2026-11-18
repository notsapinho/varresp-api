package br.com.fiap.varresp.dao;

import br.com.fiap.varresp.model.DemandaSemanal;
import br.com.fiap.varresp.model.DistritoSemanaId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemandaSemanalDao extends JpaRepository<DemandaSemanal, DistritoSemanaId> {

    List<DemandaSemanal> findByDistritoIdOrderByIdSemanaAsc(Long distritoId);

    boolean existsByDistritoId(Long distritoId);
}
