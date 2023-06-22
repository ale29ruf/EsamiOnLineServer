package gestionedatabase;

import model.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * La classe svolge le varie operazioni sul database.
 */
public enum Repository {

    REPOSITORY; //unica istanza disponibile
    private EntityManagerFactory emf;
    private EntityManager em;
    private HandlerDB proxy;


    Repository(){
        emf = Persistence.createEntityManagerFactory("MyPersistenceUnit");
        em = emf.createEntityManager();
    }

    public void setProxy(HandlerDB proxy){
        this.proxy = proxy;
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
                    d.aggiungiScelta(s);
                    em.persist(s);
                    if (testoScelta.equals(risposte.get(i)))
                        sceltaRisposta = s;
                }
                Risposta risposta = new Risposta(d, sceltaRisposta);
                em.persist(risposta);
            }
            em.getTransaction().commit();
            if(proxy != null)
                proxy.aggiornaCache();
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

    public String aggiungiUtente(Studente s){
        String codice;
        try {
            em.getTransaction().begin();
            Appello p = cercaAppello(s.getIdappello().getId()).get(0); //oggetto persistente
            s.setIdappello(p);
            codice = UUID.randomUUID().toString().replace("-", "");
            s.setCodiceappello(codice);
            em.persist(s);
            em.getTransaction().commit();
            return codice;
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

    public List<Risposta> ottieniRisposte(int idAppello){
        Appello appello = cercaAppello(idAppello).get(0);
        List<Domanda> domande = ottieniDomande(appello);
        List<Risposta> result = new LinkedList<>();
        for(Domanda domanda : domande){
            String queryString = "SELECT r FROM Risposta r WHERE r.iddomanda = :d";
            TypedQuery<Risposta> queryR = em.createQuery(queryString, Risposta.class);
            queryR.setParameter("d",domanda);
            result.add(queryR.getSingleResult());
        }

        return result;
    }

    public void rimuoviCodiceAppello(String codiceAppello) {
        try {
            em.getTransaction().begin();
            String queryString = "SELECT s FROM Studente s WHERE s.codiceappello = :codAp";
            TypedQuery<Studente> queryA = em.createQuery(queryString, Studente.class);
            queryA.setParameter("codAp", codiceAppello);
            Studente s = queryA.getSingleResult();
            em.remove(s);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }
    }

    public List<Studente> cercaStudente(String matricola, String codFiscale, Appello appello){
        String queryString = "SELECT s FROM Studente s WHERE (s.codfiscale = :codFiscale or s.matricola = :matricola) and s.idappello = :id";
        TypedQuery<Studente> queryS = em.createQuery(queryString, Studente.class);
        queryS.setParameter("codFiscale",codFiscale);
        queryS.setParameter("matricola",matricola);
        queryS.setParameter("id",appello);
        return queryS.getResultList();
    }
}
