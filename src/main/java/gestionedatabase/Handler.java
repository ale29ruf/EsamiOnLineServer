package gestionedatabase;

import converter.*;
import model.Risposta;
import support.Client;
import support.Notificatore;
import exception.AppelloAlreadyStartedException;
import exception.AppelloNotFoundException;
import exception.OperationDBException;
import exception.UtenteAlreadyRegisteredException;
import model.Appello;
import model.Domanda;
import model.Studente;
import proto.Remotemethod;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class Handler implements HandlerDB{ //service

    ConverterFactory af = ConverterFactory.FACTORY;

    Repository r = new Repository();

    //TODO Avviare un thread 30 minuti dopo l'inizio dell'appello che vada a rimuovere la lista di domande e il notificatore dalle seguenti strutture
    Map<Appello,List<Domanda>> domandeAppello = new HashMap<>();
    Map<Appello, Notificatore> notificatoreMap = new HashMap<>();
    Lock l = new ReentrantLock(); //preferisco usare il lockA al posto di collezioni concorrenti

    ScheduledExecutorService esecutore;


    public Handler(){
        esecutore = Executors.newScheduledThreadPool(30);
    }

    public String addStudent(Remotemethod.Studente studente){
        int idAppello = studente.getIdAppello();
        String matricola = studente.getMatricola();
        String codFiscale = studente.getCodFiscale();

        /*
        //Verifico che l'appello per cui si vuole registrare sia valido
        List<Appello> p = r.cercaAppello(idAppello);
        Calendar nowLess = Calendar.getInstance();
        nowLess.roll(Calendar.MINUTE,-30);
        if(p.size() != 1 || p.get(0).getOra().before(nowLess))
            throw new AppelloNotFoundException();

        //Verifico che lo studente non sia gia' presente nel database
        List<Studente> listaS = r.cercaStudente(matricola,codFiscale,p.get(0));
        if( ! listaS.isEmpty()) //posso aggiungere lo studente sul db
            throw new UtenteAlreadyRegisteredException(); // -> chain of responsibility

         */

        Studente s = r.aggiungiUtente(studente);

        if(s == null)
            throw new OperationDBException();

        return s.getCodiceappello();
    }



    public static void main(String[] args){

        Handler gestore = new Handler();

        System.out.println();
    }

    public List<Remotemethod.Appello> caricaAppelli(){
        List<Appello> appelli = r.caricaAppelli();
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
        List<Appello> appello = r.ottieniAppello(codiceAppello);
        if(appello.isEmpty()){
            throw new AppelloNotFoundException();
        }

        Appello p = appello.get(0);

        /*
        Calendar oraAppelloPlus = p.getOra();
        oraAppelloPlus.add(Calendar.MINUTE,30);
        if(confrontaDate(oraAppelloPlus,Calendar.getInstance()) < 0 && oraAppelloPlus.after(Calendar.getInstance()))
                throw new AppelloAlreadyStartedException();

         */

        l.lock();
        Notificatore notificatore = aggiornaCache(p);
        l.unlock();

        String hostname = richiesta.getHostaname();
        int port = richiesta.getPort();

        Client c = new Client(hostname,port);
        notificatore.aggiungiClient(c);

        return p.getId()+"";
    }

    private int confrontaDate(Calendar calendario1, Calendar calendario2) {
        int anno = Integer.compare(calendario1.get(Calendar.YEAR), calendario2.get(Calendar.YEAR));
        if (Integer.compare(calendario1.get(Calendar.YEAR), calendario2.get(Calendar.YEAR)) != 0) {
            return anno;
        }

        int mese = Integer.compare(calendario1.get(Calendar.MONTH), calendario2.get(Calendar.MONTH));
        if (mese != 0) {
            return mese;
        }

        return Integer.compare(calendario1.get(Calendar.DAY_OF_MONTH), calendario2.get(Calendar.DAY_OF_MONTH));
    }


    private Notificatore aggiornaCache(Appello p) {
        if(domandeAppello.get(p) == null){
            System.out.println("Creazione notificatore");
            List<Domanda> domande = r.ottieniDomande(p);
            domandeAppello.put(p, domande);

            //Converto
            List<proto.Remotemethod.Domanda> listaDomande = new LinkedList<>();
            ModelToProtoDomanda conv = (ModelToProtoDomanda) af.createConverterModel(Domanda.class);
            for(Domanda d : domande) {
                listaDomande.add(conv.convert(d));
            }
            //Creo il task
            Notificatore notificatore = new Notificatore(listaDomande);
            notificatoreMap.put(p,notificatore);
            //long timeElapse = p.getOra().getTimeInMillis() - Calendar.getInstance().getTimeInMillis();

            //Schedulo il task
            esecutore.schedule(notificatore,5000, TimeUnit.MILLISECONDS); //sostituire 5000 con timeElapse
        }
        return notificatoreMap.get(p);
    }

    @Override
    public List<Remotemethod.Risposta> inviaRisposte(int idAppello) {
        List<Risposta> risposte = r.caricaRisposte(idAppello);
        List<Remotemethod.Risposta> result = new LinkedList<>();
        ModelToProtoRisposta conv = (ModelToProtoRisposta) af.createConverterModel(Risposta.class);
        for(Risposta r : risposte){
            result.add(conv.convert(r));
        }
        return result;
    }

}
