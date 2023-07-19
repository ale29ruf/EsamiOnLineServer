package support;

import proto.Remotemethod;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Il Notificatore si occupa di notificare a una certa ora i vari client (inviandogli le domande) per ufficializzare l'inizio dell'appello.
 */
public class Notificatore implements Runnable{

    private final List<Ascoltatore> clients = new LinkedList<>();
    Lock lock = new ReentrantLock();
    private final List<Remotemethod.Domanda> domande;
    private final int intervallo = 10; //tempo in secondi -> frequenza con cui un thread deve svegliarsi e concedere l'accesso all'appello
    private int maxInterval; //tempo in minuti
    private int timeElapse;

    public Notificatore(List<Remotemethod.Domanda> domande, int maxInterval, int timeElapse){
        this.domande = domande;
        this.maxInterval = maxInterval;
        this.timeElapse = timeElapse;
    }

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(timeElapse);
        } catch (InterruptedException ignored) {}

        System.out.println("Inizio del Notificatore: "+clients.size());

        for(int i=0; i<((maxInterval*60)/intervallo); i++){
            lock.lock();
            Iterator<Ascoltatore> it = clients.iterator();
            while(it.hasNext()){
                it.next().aggiorna(domande);
                it.remove();
            }
            lock.unlock();
            try {
                TimeUnit.SECONDS.sleep(intervallo);
            } catch (InterruptedException ignored) {}

        }
    }

    public void aggiungiClient(Ascoltatore ascoltatore){
        lock.lock();
        clients.add(ascoltatore);
        lock.unlock();
    }
}
