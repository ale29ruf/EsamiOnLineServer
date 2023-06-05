package gestionedatabase;

import proto.Remotemethod;

import java.util.LinkedList;
import java.util.List;

public class ProxyHandler implements HandlerDB{
    List<Remotemethod.Appello> appelliCached;
    Handler gestore;
    boolean changed = true;

    public ProxyHandler(Handler gestore){
        this.gestore = gestore;
    }

    @Override
    public List<Remotemethod.Appello> caricaAppelli() {
        if(changed){
            appelliCached = gestore.caricaAppelli();
            changed = false;
        }
        return appelliCached;
    }
}
