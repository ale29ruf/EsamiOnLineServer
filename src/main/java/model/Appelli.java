package model;


import javax.persistence.*;

@Entity
@Table(name = "Appelli")
public class Appelli {

    @Id
    @Column(name = "id")
    int id;

    @Column(name = "data")
    String data;

    @Column(name = "ora")
    String ora;

    @Column(name = "durata")
    String durata;

    public Appelli(int id, String data, String ora, String durata) {
        this.id = id;
        this.data = data;
        this.ora = ora;
        this.durata = durata;
    }


    public Appelli() {

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
}
