package br.com.fiap.varresp.dao;

import br.com.fiap.varresp.model.DistritoSemanaId;
import br.com.fiap.varresp.model.PrevisaoSemanal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PrevisaoSemanalDao extends JpaRepository<PrevisaoSemanal, DistritoSemanaId> {

    List<PrevisaoSemanal> findByIdSemana(LocalDate semana, Sort sort);

    boolean existsByDistritoId(Long distritoId);

    @Query("""
            select p from PrevisaoSemanal p join fetch p.distrito
            where p.id.semana = :semana
            order by p.probAlta desc, p.volumePrevisto desc, p.id.idDistrito asc
            """)
    List<PrevisaoSemanal> buscarPrioridades(@Param("semana") LocalDate semana, Pageable pageable);
}
