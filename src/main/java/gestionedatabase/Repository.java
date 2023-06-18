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
import java.util.UUID;

public class Repository { //opera sul DB

    EntityManagerFactory emf;
    EntityManager em;

    ConverterFactory af = ConverterFactory.FACTORY;

    public Repository(){
        emf = Persistence.createEntityManagerFactory("MyPersistenceUnit");
        em = emf.createEntityManager();
    }

    public void aggiungiAppello(Appello appello){
        try {
            em.getTransaction().begin();
            em.persist(appello);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }

    }

    public void aggiungiRisposta(int idDomanda, int idScelta){
        Domanda d = em.find(Domanda.class,idDomanda);
        Scelta s = em.find(Scelta.class, idScelta);
        Risposta r = new Risposta();
        r.setIddomanda(d);
        r.setRisposta(s);

        try {

            em.getTransaction().begin();
            em.persist(r);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }
    }

    public static void main(String[] args){
        Repository r = new Repository();
        r.aggiungiRisposta(1,1);




        /*
        Appello appello = new Appello();
        appello.setId(12);
        appello.setNome("Appello prossimo-esimo");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2023);
        calendar.set(Calendar.MONTH, Calendar.JUNE);
        calendar.set(Calendar.DAY_OF_MONTH, 12);
        calendar.set(Calendar.HOUR_OF_DAY, 16);
        calendar.set(Calendar.MINUTE, 40);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        appello.setOra(calendar);
        appello.setDurata("2 ore");
        r.aggiungiAppello(appello);
        System.out.println("aggiunto appello");

         */



        /*
        for (int i=0; i<5; i++){
            Appello appello = new Appello();
            appello.setId(i);
            appello.setNome("Appello "+i+"-esimo");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, 2023);
            calendar.set(Calendar.MONTH, Calendar.JULY);
            calendar.set(Calendar.DAY_OF_MONTH, 20);
            calendar.set(Calendar.HOUR_OF_DAY, 10);
            calendar.set(Calendar.MINUTE, 30);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            appello.setOra(calendar);
            appello.setDurata("2 ore");
            r.aggiungiAppello(appello);
            System.out.println("aggiunto appello");
        }



        Appello ap = r.cercaAppello(0).get(0);
        List<Domanda> domande = r.ottieniDomande(ap);
        for(Domanda d : domande){
            System.out.println(d.getTesto());
            for(Scelte s : d.getScelte())
                System.out.println(s.getTesto());
        }*/


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

    public List<Studente> cercaStudente(String matricola, String codFiscale, Appello appello){
        String queryString = "SELECT s FROM Studente s WHERE (s.codfiscale = :codFiscale or s.matricola = :matricola) and s.idappello = :id";
        TypedQuery<Studente> queryS = em.createQuery(queryString, Studente.class);
        queryS.setParameter("codFiscale",codFiscale);
        queryS.setParameter("matricola",matricola);
        queryS.setParameter("id",appello);
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
