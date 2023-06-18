package model;

import javax.persistence.*;

@Entity
@Table(name = "scelte")
public class Scelta implements Models{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Integer id;

    @Column(name = "testo", length = 50)
    private String testo;

    @ManyToOne
    @JoinColumn(name = "domanda")
    private Domanda domanda;

    public Scelta(String testo, Domanda d){
        this.testo = testo;
        this.domanda = d;
    }

    public Scelta() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public Domanda getDomanda() {
        return domanda;
    }

    public void setDomanda(Domanda domanda) {
        this.domanda = domanda;
    }

}