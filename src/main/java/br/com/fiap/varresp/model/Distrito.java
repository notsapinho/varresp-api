package br.com.fiap.varresp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "distrito")
public class Distrito {

    @Id
    @Column(name = "id_distrito")
    private Long id;

    @Column(name = "nome", nullable = false, unique = true, length = 80)
    private String nome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
