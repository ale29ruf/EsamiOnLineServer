package gestionedatabase;

import exception.AppelloNotFoundException;
import exception.UtenteAlreadyRegisteredException;
import proto.Remotemethod;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


public class ProxyHandler implements HandlerDB{

    List<Remotemethod.Appello> appelliCached;
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
}
