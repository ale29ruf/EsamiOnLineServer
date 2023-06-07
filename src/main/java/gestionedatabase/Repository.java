package gestionedatabase;

import abstractfactory.AbstractFactory;
import abstractfactory.AppelloFactory;
import model.Appello;
import model.Studente;
import proto.Remotemethod;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;

public class Repository { //opera sul DB

    EntityManagerFactory emf;
    EntityManager em;

    AbstractFactory af = AppelloFactory.FACTORY;

    public Repository(){
        emf = Persistence.createEntityManagerFactory("MyPersistenceUnit");
        em = emf.createEntityManager();
    }

    public List<Appello> caricaAppelli(){
        String queryString = "SELECT e FROM Appello e";
        TypedQuery<Appello> query = em.createQuery(queryString, Appello.class);
        return query.getResultList();
    }

    public List<Appello> cercaAppello(int id){
        String queryString = "SELECT a FROM Appello a WHERE id = :idAppello";
        TypedQuery<Appello> query = em.createQuery(queryString, Appello.class);
        query.setParameter("idAppello", id);
        return query.getResultList();
    }

    public List<Studente> cercaStudente(String matricola, String codFiscale){
        String queryString = "SELECT s FROM Studente s WHERE s.codfiscale = :codFiscale or s.matricola = :matricola";
        TypedQuery<Studente> queryS = em.createQuery(queryString, Studente.class);
        queryS.setParameter("codFiscale",codFiscale);
        queryS.setParameter("matricola",matricola);
        return queryS.getResultList();
    }

    public Studente aggiungiUtente(Remotemethod.Studente studente){
        Studente s = null;
        try {
            em.getTransaction().begin();
            Appello p = cercaAppello(studente.getIdAppello()).get(0); //oggetto persistente
            s = (Studente) af.createModel(studente,p, UUID.randomUUID().toString().replace("-", ""));
            em.persist(s);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            return null;
        } finally {
            em.close();
        }
        return s;
    }
}
