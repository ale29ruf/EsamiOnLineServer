package model;


import javax.persistence.*;

@Entity
@Table(name = "studenti")
public class Studente implements Models{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private int id; //long andrebbe meglio

    @Column(name = "matricola", length = 10)
    private String matricola;

    @Column(name = "codfiscale", length = 20)
    private String codfiscale;

    @Column(name = "codiceappello", length = 36)
    private String codiceappello; //potrebbe esserci uno stesso studente per appelli diversi

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idappello")
    private Appello idappello;

    public Appello getIdappello() {
        return idappello;
    }

    public void setIdappello(Appello idappello) {
        this.idappello = idappello;
    }

    public String getCodiceappello() {
        return codiceappello;
    }

    public void setCodiceappello(String codiceappello) {
        this.codiceappello = codiceappello;
    }

    public String getCodfiscale() {
        return codfiscale;
    }

    public void setCodfiscale(String codfiscale) {
        this.codfiscale = codfiscale;
    }

    public String getMatricola() {
        return matricola;
    }

    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isStudente(){
        return true;
    }
}
