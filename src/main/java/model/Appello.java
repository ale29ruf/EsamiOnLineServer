package model;


import javax.persistence.*;

@Entity
@Table(name = "appelli")
public class Appello implements Models {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    int id;

    @Column(name = "data")
    String data;

    @Column(name = "ora")
    String ora;

    @Column(name = "durata")
    String durata;

    public Appello(int id, String data, String ora, String durata) {
        this.id = id;
        this.data = data;
        this.ora = ora;
        this.durata = durata;
    }


    public Appello() {

    }

    @Override
    public boolean isAppello() { //evito runtime type identification
        return true;
    }

    public int getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public String getOra() {
        return ora;
    }

    public String getDurata() {
        return durata;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }

    public void setDurata(String durata) {
        this.durata = durata;
    }
}
