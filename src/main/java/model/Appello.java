package model;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;

@Entity
@Table(name = "appelli")
public class Appello implements Models {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    int id;

    @Column(name = "ora")
    Calendar ora;

    @Column(name = "durata")
    String durata;

    public Appello(int id, Calendar ora, String durata) {
        this.id = id;
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

    public Calendar getOra() {
        return ora;
    }

    public String getDurata() {
        return durata;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOra(Calendar ora) {
        this.ora = ora;
    }

    public void setDurata(String durata) {
        this.durata = durata;
    }
}
