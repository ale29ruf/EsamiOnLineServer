package gestionedatabase;

import model.Appelli;
import proto.Remotemethod;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public final class Handler implements HandlerDB{


    public Handler(){


    }

    public boolean addStudent(int appello, Remotemethod.CodiceAppello cod){
        String codice = cod.getCodice();

        return true;
    }

    public List<Remotemethod.Appello> caricaAppelli(){

        return null;
    }

    public List<Remotemethod.Domanda> caricaDomandeAppello(int idAppello){
        //QUERY
        return null;
    }

    public  List<Remotemethod.Risposta> ottieniRisposte(int idAppello){
        //QUERY
        return null;
    }

    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MyPersistenceUnit");
        EntityManager em = emf.createEntityManager();

        // Esegui la query per ottenere tutte le tuple della tabella
        String queryString = "SELECT e FROM Appelli e";
        TypedQuery<Appelli> query = em.createQuery(queryString, Appelli.class);
        List<Appelli> risultati = query.getResultList();
        System.out.println("Query eseguita");
        System.out.println(risultati.size());
        // Itera sui risultati
        for (Appelli ap : risultati) {
            // Fai qualcosa con ogni tupla ottenuta
            System.out.println(ap.getId() + ap.getData() + ap.getDurata() + ap.getOra());
        }

        em.close();
        emf.close();
    }
}
