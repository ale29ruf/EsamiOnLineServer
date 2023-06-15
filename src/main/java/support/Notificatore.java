package support;

import proto.Remotemethod;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Notificatore implements Runnable{

    List<Ascoltatore> clients = Collections.synchronizedList(new LinkedList<>());
    List<Remotemethod.Domanda> domande;

    public Notificatore(List<Remotemethod.Domanda> domande){
        this.domande = domande;
    }

    @Override
    public void run() {
        while(true){
            for(Ascoltatore ascoltatore : clients){
                ascoltatore.aggiorna(domande);
                clients.remove(ascoltatore);
            }

            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException ignored) {

            }
            System.out.println("Sto per aggiornare i client aggiunti");
        }

    }

    public void aggiungiClient(Ascoltatore ascoltatore){
        System.out.println("Aggiunto nuovo client");
        clients.add(ascoltatore);
    }
}
