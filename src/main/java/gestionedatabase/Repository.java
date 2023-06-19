package gestionedatabase;

import converter.ConverterFactory;
import converter.ProtoToModelStudente;
import model.*;
import proto.Remotemethod;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public enum Repository { //opera sul DB

    REPOSITORY;
    EntityManagerFactory emf;
    EntityManager em;

    ConverterFactory af = ConverterFactory.FACTORY;

    Repository(){
        emf = Persistence.createEntityManagerFactory("MyPersistenceUnit");
        em = emf.createEntityManager();
    }

    public boolean aggiungiAppelloCompleto(Appello appello, List<String> domande, Map<Integer,List<String>> scelte,Map<Integer,String> risposte){
        try {
            em.getTransaction().begin();
            em.persist(appello);
            for (int i = 0; i < domande.size(); i++) {
                Domanda d = new Domanda(domande.get(i), appello);
                em.persist(d);
                List<String> scelteDomande = scelte.get(i);
                Scelta sceltaRisposta = null;
                for (String testoScelta : scelteDomande) {
                    Scelta s = new Scelta(testoScelta, d);
                    em.persist(s);
                    if (testoScelta.equals(risposte.get(i)))
                        sceltaRisposta = s;
                }
                Risposta risposta = new Risposta(d, sceltaRisposta);
                em.persist(risposta);
            }
            em.getTransaction().commit();
            return true;
        } catch (Exception e){
            em.getTransaction().rollback();
        }
        return false;
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

    public List<Appello> cercaAppelloPerNome(String nomeAppello){
        String queryString = "SELECT a FROM Appello a WHERE nome = :nomeAppello";
        TypedQuery<Appello> query = em.createQuery(queryString, Appello.class);
        query.setParameter("nomeAppello", nomeAppello);
        return query.getResultList();
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
            return s;
        } catch (Exception e) {
            em.getTransaction().rollback();
        }
        return null;
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
        queryD.setParameter("idAppello", p);
        return queryD.getResultList();
    }

    public List<Risposta> caricaRisposte(int idAppello){
        Appello appello = cercaAppello(idAppello).get(0);
        String queryString = "SELECT r FROM Domanda d, Risposta r WHERE d.appello = :appello AND r.iddomanda = d";
        TypedQuery<Risposta> queryR = em.createQuery(queryString, Risposta.class);
        queryR.setParameter("appello",appello);
        return queryR.getResultList();
    }
}
