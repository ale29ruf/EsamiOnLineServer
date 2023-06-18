package model;

import javax.persistence.*;

@Entity
@Table(name = "risposte")
public class Risposta implements Models{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "iddomanda")
    private Domanda iddomanda;

    @OneToOne
    @JoinColumn(name = "scelta")
    private Scelta scelta;

    public Risposta(Domanda d, Scelta s){
        this.scelta = s;
        this.iddomanda = d;
    }

    public Risposta(){
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getIddomanda() {
        return iddomanda.getId();
    }

    public void setIddomanda(Domanda iddomanda) {
        this.iddomanda = iddomanda;
    }

    public int getScelta() {
        return scelta.getId();
    }

    public String getTestoScelta(){
        return scelta.getTesto();
    }

    public void setRisposta(Scelta scelta) {
        this.scelta = scelta;
    }

    @Override
    public boolean isRisposta(){
        return true;
    }

}