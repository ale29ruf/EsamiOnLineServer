package model;

import javax.persistence.*;

@Entity
@Table(name = "risposte")
public class Risposta {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "iddomanda")
    private Domanda iddomanda;

    @Column(name = "risposta")
    private Integer risposta;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Domanda getIddomanda() {
        return iddomanda;
    }

    public void setIddomanda(Domanda iddomanda) {
        this.iddomanda = iddomanda;
    }

    public Integer getRisposta() {
        return risposta;
    }

    public void setRisposta(Integer risposta) {
        this.risposta = risposta;
    }

}