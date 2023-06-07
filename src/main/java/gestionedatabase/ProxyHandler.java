package gestionedatabase;

import exception.AppelloAlreadyStartedException;
import exception.AppelloNotFoundException;
import exception.UtenteAlreadyRegisteredException;
import proto.Remotemethod;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


public class ProxyHandler implements HandlerDB{

    List<Remotemethod.Appello> appelliCached; //utilizzo di una cache in modo da condividere la lista di appelli (flyweight)
    Handler gestore;
    boolean changed = true;
    ReentrantLock lock = new ReentrantLock();


    public ProxyHandler(Handler gestore){
        this.gestore = gestore;
    }

    @Override
    public List<Remotemethod.Appello> caricaAppelli() {
        lock.lock();
        if(changed){
            appelliCached = gestore.caricaAppelli();
            changed = false;
        }
        lock.unlock();
        return appelliCached;
    }

    @Override
    public String addStudent(Remotemethod.Studente studente){
        String res;
        try{
            res = gestore.addStudent(studente);
        } catch (AppelloNotFoundException e){
            res = "Nessun appello trovato.";
            System.out.println(res); //-> invia info al logger e sollevare una opportuna eccezione
        } catch (UtenteAlreadyRegisteredException e){
            res = "Utente gia' registrato";
            System.out.println(res); //-> invia info al logger e sollevare una opportuna eccezione
        }

        return res;
    }

    @Override
    public String partecipaEsame(Remotemethod.pRequest richiesta) {
        String risposta = "";
        try{
            gestore.partecipaEsame(richiesta);
        } catch (AppelloNotFoundException e){
            risposta = "Nessun appello trovato per il corrispettivo codice inviato.";
        } catch (AppelloAlreadyStartedException e){
            risposta = "Appello gia' iniziato. Impossibile partecipare.";
        }

        return risposta;
    }


}
