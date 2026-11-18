package br.com.fiap.varresp.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "demanda_semanal")
public class DemandaSemanal {

    @EmbeddedId
    private DistritoSemanaId id;

    @MapsId("idDistrito")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_distrito")
    private Distrito distrito;

    @Column(name = "pedidos", nullable = false)
    private Integer pedidos;

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

    public Integer getPedidos() {
        return pedidos;
    }

    public void setPedidos(Integer pedidos) {
        this.pedidos = pedidos;
    }
}
