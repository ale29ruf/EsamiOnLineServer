package model;

import javax.persistence.*;

@Entity
@Table(name = "domande")
public class Domanda implements Models{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "testo", length = 50)
    private String testo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appello")
    private Appello appello;

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


    @Override
    public boolean isDomanda(){
        return true;
    }

}