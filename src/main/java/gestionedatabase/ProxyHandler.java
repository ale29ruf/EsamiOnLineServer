package gestionedatabase;

import exception.AppelloAlreadyStartedException;
import exception.AppelloNotFoundException;
import exception.OperationDBException;
import exception.UtenteAlreadyRegisteredException;
import proto.Remotemethod;

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


    public ProxyHandler(Handler gestore){
        this.gestore = gestore;
    }

    @Override
    public List<Remotemethod.Appello> caricaAppelli() {
        lockA.lock();
        if(changed){
            appelliCached = gestore.caricaAppelli();
            changed = false;
        }
        lockA.unlock();
        return appelliCached;
    }

    @Override
    public String addStudent(Remotemethod.Studente studente){
        String res;
        try{
            res = gestore.addStudent(studente);
        } catch (AppelloNotFoundException e){
            res = "ERRORE: Nessun appello trovato.";
            System.out.println(res); //-> invia info al logger e sollevare una opportuna eccezione
        } catch (UtenteAlreadyRegisteredException e){
            res = "ERRORE: Utente gia' registrato";
            System.out.println(res); //-> invia info al logger e sollevare una opportuna eccezione
        } catch (OperationDBException e){
            res = "ERRORE: Riprova operazione piu' tardi";
            System.out.println(res); //-> invia info al logger e sollevare una opportuna eccezione
        }
        return res;
    }

    @Override
    public String partecipaEsame(Remotemethod.pRequest richiesta) {
        String risposta = "";
        try{
            risposta = gestore.partecipaEsame(richiesta);
        } catch (AppelloNotFoundException e){
            risposta = "ERRORE: Nessun appello trovato per il corrispettivo codice inviato";
        } catch (AppelloAlreadyStartedException e){
            risposta = "ERRORE: Appello gia' iniziato. Impossibile partecipare";
        }

        return risposta;
    }

    @Override
    public List<Remotemethod.Risposta> inviaRisposte(int idAppello) {
        List<Remotemethod.Risposta> risposte;
        try{
            lockR.lock();
            risposte = risposteCached.get(idAppello);
            if(risposte == null){
                risposteCached.put(idAppello,gestore.inviaRisposte(idAppello));
            }
        }finally{
            lockR.unlock();
        }
        return risposte;
    }


}
