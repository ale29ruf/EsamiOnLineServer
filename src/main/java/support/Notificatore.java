package support;

import proto.Remotemethod;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Notificatore implements Runnable{

    List<Ascoltatore> clients = Collections.synchronizedList(new LinkedList<>());
    List<Remotemethod.Domanda> domande;

    public Notificatore(List<Remotemethod.Domanda> domande){
        this.domande = domande;
    }

    @Override
    public void run() {
        for(Ascoltatore ascoltatore : clients)
            ascoltatore.aggiorna(domande);
    }

    public void aggiungiClient(Ascoltatore ascoltatore){
        clients.add(ascoltatore);
    }
}
