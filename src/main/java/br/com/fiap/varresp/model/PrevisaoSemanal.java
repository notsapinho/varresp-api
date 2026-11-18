package br.com.fiap.varresp.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "previsao_semanal")
public class PrevisaoSemanal {

    @EmbeddedId
    private DistritoSemanaId id;

    @MapsId("idDistrito")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_distrito")
    private Distrito distrito;

    @Column(name = "volume_previsto", nullable = false, precision = 10, scale = 3)
    private BigDecimal volumePrevisto;

    @Column(name = "prob_alta", nullable = false, precision = 7, scale = 6)
    private BigDecimal probAlta;

    @Column(name = "limite_alta", nullable = false)
    private Integer limiteAlta;

    public DistritoSemanaId getId() {
        return id;
    }

    public void setId(DistritoSemanaId id) {
        this.id = id;
    }

    public Distrito getDistrito() {
        return distrito;
    }

    public void setDistrito(Distrito distrito) {
        this.distrito = distrito;
    }

    public BigDecimal getVolumePrevisto() {
        return volumePrevisto;
    }

    public void setVolumePrevisto(BigDecimal volumePrevisto) {
        this.volumePrevisto = volumePrevisto;
    }

    public BigDecimal getProbAlta() {
        return probAlta;
    }

    public void setProbAlta(BigDecimal probAlta) {
        this.probAlta = probAlta;
    }

    public Integer getLimiteAlta() {
        return limiteAlta;
    }

    public void setLimiteAlta(Integer limiteAlta) {
        this.limiteAlta = limiteAlta;
    }
}
