package gestionedatabase;

import exception.AppelloAlreadyStartedException;
import exception.AppelloNotFoundException;
import exception.OperationDBException;
import exception.UtenteAlreadyRegisteredException;
import proto.Remotemethod;
import servergui.SyncronizedJTextArea;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * La classe svolge il ruolo di proxy nei confronti dell'Handler.
 */
public class ProxyHandler implements HandlerDB{

    private List<Remotemethod.Appello> appelliCached; //utilizzo di una cache in modo da condividere la lista di appelli
    private ReentrantLock lockA = new ReentrantLock(); //lock usato su appelliCached

    private final Handler gestore;
    private boolean changed = true; //utilizzata nel caso in cui fosse aggiunto un nuovo appello

    private final Map<Integer,List<Remotemethod.Risposta>> risposteCached = new HashMap<>(); //caching delle risposte
    private ReentrantLock lockR = new ReentrantLock(); //lock su risposteCached

    private SyncronizedJTextArea logger; //logger a cui comunichiamo le varie attività

    public ProxyHandler(Handler gestore){
        this.gestore = gestore;
    }

    public void setLogger(SyncronizedJTextArea logger){
        this.logger = logger;
    }

    @Override
    public void aggiornaCache(){ //metodo invocato dal Repository dopo aver aggiunto un nuovo appello
        try{
            System.out.println("Prima del lock");
            lockA.lock();
            System.out.println("Dopo del lock");
            changed = true;
        } finally {
            lockA.unlock();

        }
    }

    @Override
    public List<Remotemethod.Appello> caricaAppelli() {
        lockA.lock();
        if(changed){
            appelliCached = gestore.caricaAppelli();
            changed = false;
        }
        lockA.unlock();
        logger.segnala("Nuova richiesta di caricamento appello \n");
        return appelliCached;
    }

    @Override
    public String addStudent(Remotemethod.Studente studente){
        String res;
        try{
            res = gestore.addStudent(studente);
            logger.segnala("Richiesta di prenotazione >> "+studente+" conclusa con successo \n");
        } catch (AppelloNotFoundException e){
            res = "ERRORE: Nessun appello trovato.";
            logger.segnala("Richiesta di prenotazione >> "+studente+" conclusa con "+res+" \n");
        } catch (UtenteAlreadyRegisteredException e){
            res = "ERRORE: Utente già registrato";
            logger.segnala("Richiesta di prenotazione >> "+studente+" conclusa con "+res+" \n");
        } catch (OperationDBException e){
            res = "ERRORE: Riprova operazione piu' tardi";
            logger.segnala("Richiesta di prenotazione >> "+studente+" conclusa con "+res+" \n");
        }
        return res;
    }

    @Override
    public String partecipaEsame(Remotemethod.pRequest richiesta) {
        String risposta = "";
        try{
            risposta = gestore.partecipaEsame(richiesta);
            logger.segnala("Richiesta di partecipazione esame >> "+richiesta+" conclusa con successo \n");
        } catch (AppelloNotFoundException e){
            risposta = "ERRORE: Nessun appello trovato per il corrispettivo codice inviato";
            logger.segnala("Richiesta di partecipazione esame >> "+richiesta+" conclusa con "+risposta+" \n");
        } catch (AppelloAlreadyStartedException e){
            risposta = "ERRORE: Appello gia' iniziato. Impossibile partecipare";
            logger.segnala("Richiesta di partecipazione esame >> "+richiesta+" conclusa con "+risposta+" \n");
        }
        return risposta;
    }

    @Override
    public List<Remotemethod.Risposta> inviaRisposte(int idAppello) {
        try{
            lockR.lock();
            if(risposteCached.get(idAppello) == null){
                risposteCached.put(idAppello,gestore.inviaRisposte(idAppello));
            }
        }finally{
            lockR.unlock();
        }
        logger.segnala("Appello con id "+idAppello+" concluso ... invio delle risposte \n");
        return risposteCached.get(idAppello);
    }


}
