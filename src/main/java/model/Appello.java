package model;


import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "appelli")
public class Appello implements Models {

    @Id
    @Column(name = "id")
    int id;

    @Column(name = "nome")
    String nome;

    @Column(name = "ora")
    Calendar ora;

    @Column(name = "durata")
    String durata;

    public Appello(int id, String nome, Calendar ora, String durata) {
        this.id = id;
        this.nome = nome;
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
    public String getNome() {
        return nome;
    }

    public Calendar getOra() {
        return ora;
    }

    public String getDurata() {
        return durata;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setOra(Calendar ora) {
        this.ora = ora;
    }

    public void setDurata(String durata) {
        this.durata = durata;
    }

    @Override
    public String toString() {
        return "Appello{" +
                "id=" + id +
                '}';
    }
}
