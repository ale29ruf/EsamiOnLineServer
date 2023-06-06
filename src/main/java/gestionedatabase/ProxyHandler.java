package gestionedatabase;

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
}
