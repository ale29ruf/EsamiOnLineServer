package model;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appelli")
public class Appello implements Models {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    int id;

    @Column(name = "data")
    LocalDate data;

    @Column(name = "ora")
    LocalTime ora;

    @Column(name = "durata")
    String durata;

    public Appello(int id, LocalDate data, LocalTime ora, String durata) {
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

    public LocalDate getData() {
        return data;
    }

    public LocalTime getOra() {
        return ora;
    }

    public String getDurata() {
        return durata;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setOra(LocalTime ora) {
        this.ora = ora;
    }

    public void setDurata(String durata) {
        this.durata = durata;
    }
}
