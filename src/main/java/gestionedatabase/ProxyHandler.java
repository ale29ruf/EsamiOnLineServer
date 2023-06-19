package gestionedatabase;

import exception.AppelloAlreadyStartedException;
import exception.AppelloNotFoundException;
import exception.OperationDBException;
import exception.UtenteAlreadyRegisteredException;
import proto.Remotemethod;
import servergui.SyncronizedJTextArea;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;


public class ProxyHandler implements HandlerDB{

    List<Remotemethod.Appello> appelliCached; //utilizzo di una cache in modo da condividere la lista di appelli (flyweight)
    ReentrantLock lockA = new ReentrantLock();

    Handler gestore;
    boolean changed = true;

    Map<Integer,List<Remotemethod.Risposta>> risposteCached = new HashMap<>();
    ReentrantLock lockR = new ReentrantLock();

    SyncronizedJTextArea logger;

    public ProxyHandler(Handler gestore){
        this.gestore = gestore;
    }

    public void setLogger(SyncronizedJTextArea logger){
        this.logger = logger;
    }

    public void aggiornaCache(){
        try{
            lockA.lock();
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
            res = "ERRORE: Utente gia' registrato";
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
