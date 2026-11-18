package br.com.fiap.varresp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class DistritoSemanaId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "id_distrito")
    private Long idDistrito;

    @Column(name = "semana")
    private LocalDate semana;

    protected DistritoSemanaId() {
    }

    public DistritoSemanaId(Long idDistrito, LocalDate semana) {
        this.idDistrito = idDistrito;
        this.semana = semana;
    }

    public Long getIdDistrito() {
        return idDistrito;
    }

    public LocalDate getSemana() {
        return semana;
    }

    @Override
    public boolean equals(Object outro) {
        if (this == outro) {
            return true;
        }
        if (!(outro instanceof DistritoSemanaId)) {
            return false;
        }
        DistritoSemanaId that = (DistritoSemanaId) outro;
        return Objects.equals(idDistrito, that.idDistrito) && Objects.equals(semana, that.semana);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDistrito, semana);
    }
}
