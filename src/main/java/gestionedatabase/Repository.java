package gestionedatabase;

import converter.ConverterFactory;
import converter.ProtoToModelStudente;
import model.Appello;
import model.Domanda;
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

    ConverterFactory af = ConverterFactory.FACTORY;

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
        Studente s;
        try {
            em.getTransaction().begin();
            Appello p = cercaAppello(studente.getIdAppello()).get(0); //oggetto persistente
            ProtoToModelStudente conv = (ProtoToModelStudente) af.createConverterProto(Remotemethod.Studente.class);
            s = (Studente) conv.convert(studente,p, UUID.randomUUID().toString().replace("-", ""));
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

    public List<Appello> ottieniAppello(String codiceAppello) {
        String queryString = "SELECT s.idappello FROM Studente s WHERE s.codiceappello = :codAp";
        TypedQuery<Appello> queryA = em.createQuery(queryString, Appello.class);
        queryA.setParameter("codAp", codiceAppello);
        return queryA.getResultList();

    }

    public List<Domanda> ottieniDomande(Appello p) {
        String queryString = "SELECT d FROM Domanda d WHERE d.appello = :idAppello";
        TypedQuery<Domanda> queryD = em.createQuery(queryString, Domanda.class);
        queryD.setParameter("idAppello", p.getId());
        return queryD.getResultList();
    }
}
