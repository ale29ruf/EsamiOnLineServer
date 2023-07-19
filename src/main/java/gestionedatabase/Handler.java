package gestionedatabase;

import converter.*;
import exception.AppelloAlreadyStartedException;
import exception.UtenteAlreadyRegisteredException;
import model.Risposta;
import support.Client;
import support.Notificatore;
import exception.AppelloNotFoundException;
import exception.OperationDBException;
import model.Appello;
import model.Domanda;
import model.Studente;
import proto.Remotemethod;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public final class Handler implements HandlerDB{ //Servizio principale

    private final ConverterFactory af = ConverterFactory.FACTORY; //utilizzato per effettuare le varie conversioni
    private final Repository repository = Repository.REPOSITORY; //utilizzato per accedere al database

    private Map<Appello,List<Domanda>> domandeAppello = new HashMap<>();
    private Map<Appello, Notificatore> notificatoreMap = new HashMap<>();
    private Lock l = new ReentrantLock(); //preferisco usare il lock al posto di collezioni concorrenti
    private final int maxInterval = 15; //tempo massimo, in minuti, dall'inizio dell'appello, che consente agli utenti di partecipare
    private final int ritardoPartecipazioneAppello = 5; //tempo in minuti oltre il quale, dall'inizio dell'appello, non è piu' possibile partecipare
    private final int limitePrenotazioneAppello = 5; //tempo in minuti prima dell'inizio dell'appello che consente di prenotarsi

    ScheduledExecutorService esecutore;


    public Handler(){
        esecutore = Executors.newScheduledThreadPool(30);
    }

    public String addStudent(Remotemethod.Studente studente){
        int idAppello = studente.getIdAppello();
        String matricola = studente.getMatricola();
        String codFiscale = studente.getCodFiscale();


        //Verifico che l'appello per cui si vuole registrare sia valido
        List<Appello> listaA = repository.cercaAppello(idAppello);
        if(listaA.size() != 1) {
            throw new AppelloNotFoundException();
        }

        //Verifico che lo studente non sia gia' presente nel database
        List<Studente> listaS = repository.cercaStudente(matricola,codFiscale, listaA.get(0));
        if( ! listaS.isEmpty()) //posso aggiungere lo studente sul db
            throw new UtenteAlreadyRegisteredException();

        //Verifico che lo studente possa ancora prenotarsi
        /* Disabilitare per debug. Da attivare per testing.
        Appello p = listaA.get(0);
        int timeElapse = diffTimeFromNow(p);
        if(timeElapse <= limitePrenotazioneAppello*60)
            throw new AppelloNotFoundException();

         */

        ProtoToModelStudente conv = (ProtoToModelStudente) af.createConverterProto(Remotemethod.Studente.class);
        Studente s = (Studente) conv.convert(studente);
        String codiceAppello = repository.aggiungiUtente(s);

        if(s == null)
            throw new OperationDBException();

        return codiceAppello;
    }

    public List<Remotemethod.Appello> caricaAppelli(){
        List<Appello> appelli = repository.caricaAppelli();
        List<Remotemethod.Appello> result = new LinkedList<>();
        ModelToProtoAppello conv = (ModelToProtoAppello) af.createConverterModel(Appello.class);
        for(Appello ap : appelli) {
            result.add(conv.convert(ap));
        }
        return result;
    }

    @Override
    public String partecipaEsame(Remotemethod.pRequest richiesta) {
        String codiceAppello = richiesta.getCodApello().toString().substring(9,10+31);
        List<Appello> appello = repository.ottieniAppello(codiceAppello);
        if(appello.isEmpty()){
            repository.rimuoviCodiceAppello(codiceAppello);
            throw new AppelloNotFoundException();
        }

        Appello p = appello.get(0);

        /*
        int timeElapse = diffTimeFromNow(p);
        if(timeElapse < 0 && Math.abs(timeElapse) > ritardoPartecipazioneAppello*60) { //Consentiamo la partecipazione ad un appello fino a ritardoPartecipazioneAppello minuti dall'inizio
            repository.rimuoviCodiceAppello(codiceAppello);
            throw new AppelloAlreadyStartedException();
        }

         */

        repository.rimuoviCodiceAppello(codiceAppello);

        l.lock();
        Notificatore notificatore = aggiornaCache(p);
        l.unlock();

        String hostname = richiesta.getHostaname();
        int port = richiesta.getPort();

        Client c = new Client(hostname,port);
        notificatore.aggiungiClient(c);

        return p.getId()+"";
    }


    private Notificatore aggiornaCache(Appello p) { //Verifico se il notificatore che gestisce un determinato appello è già stato caricato
        if(domandeAppello.get(p) == null){
            List<Domanda> domande = repository.ottieniDomande(p);
            domandeAppello.put(p, domande);
            //Converto
            List<proto.Remotemethod.Domanda> listaDomande = new LinkedList<>();
            ModelToProtoDomanda conv = (ModelToProtoDomanda) af.createConverterModel(Domanda.class);
            for(Domanda d : domande) {
                listaDomande.add(conv.convert(d));
            }

            int timeElapse = diffTimeFromNow(p);
            if(timeElapse < 0) timeElapse = 0;

            //Creo il task
            Notificatore notificatore = new Notificatore(listaDomande,maxInterval);
            notificatoreMap.put(p,notificatore);
            esecutore.schedule(notificatore,timeElapse, TimeUnit.SECONDS);
        }
        return notificatoreMap.get(p);
    }

    public int diffTimeFromNow(Appello p){
        int tempoInSecAppello = p.getOra().get(Calendar.HOUR)*60*60 + p.getOra().get(Calendar.MINUTE)*60 + p.getOra().get(Calendar.SECOND);
        int timeNow = Calendar.getInstance().get(Calendar.HOUR)*60*60 + Calendar.getInstance().get(Calendar.MINUTE)*60 + Calendar.getInstance().get(Calendar.SECOND);

        return tempoInSecAppello - timeNow;
    }

    @Override
    public List<Remotemethod.Risposta> inviaRisposte(int idAppello) {
        List<Risposta> risposte = repository.ottieniRisposte(idAppello);
        List<Remotemethod.Risposta> result = new LinkedList<>();
        ModelToProtoRisposta conv = (ModelToProtoRisposta) af.createConverterModel(Risposta.class);
        for(Risposta r : risposte){
            result.add(conv.convert(r));
        }
        return result;
    }

}
