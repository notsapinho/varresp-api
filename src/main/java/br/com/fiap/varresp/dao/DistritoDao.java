package br.com.fiap.varresp.dao;

import br.com.fiap.varresp.model.Distrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DistritoDao extends JpaRepository<Distrito, Long> {

    boolean existsByNome(String nome);

    boolean existsByNomeAndIdNot(String nome, Long id);

    @Query("select coalesce(max(d.id), 0L) from Distrito d")
    Long maiorId();
}
