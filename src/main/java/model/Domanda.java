package model;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "domande")
public class Domanda implements Models{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "testo", length = 50)
    private String testo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "appello")
    private Appello appello;

    @OneToMany(mappedBy = "domanda")
    private List<Scelta> scelta;

    public Domanda(String testo,Appello p){
        this.appello = p;
        this.testo = testo;
        this.scelta = new LinkedList<>();
    }

    public Domanda() {

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

    public Appello getAppello() {
        return appello;
    }

    public void setAppello(Appello appello) {
        this.appello = appello;
    }

    public void setScelte(List<Scelta> scelta){
        this.scelta = scelta;
    }

    public void aggiungiScelta(Scelta s){
        scelta.add(s);
    }

    public List<Scelta> getScelte() {
        return scelta;
    }

    @Override
    public boolean isDomanda(){
        return true;
    }

}