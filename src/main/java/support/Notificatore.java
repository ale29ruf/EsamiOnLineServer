package support;

import proto.Remotemethod;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Notificatore implements Runnable{

    List<Ascoltatore> clients = Collections.synchronizedList(new LinkedList<>());
    List<Remotemethod.Domanda> domande;
    final int intervallo = 10; //tempo in secondi -> frequenza con cui un thread deve svegliarsi e concedere l'accesso all'appello
    int maxInterval; //tempo in minuti

    public Notificatore(List<Remotemethod.Domanda> domande, int maxInterval){
        this.domande = domande;
        this.maxInterval = maxInterval;
    }

    @Override
    public void run() {
        //for(int i=0; i<((maxInterval*60)/intervallo); i++){
        while(true){
            for(Ascoltatore ascoltatore : clients){
                ascoltatore.aggiorna(domande);
                clients.remove(ascoltatore);
            }

            try {
                TimeUnit.SECONDS.sleep(intervallo);
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
