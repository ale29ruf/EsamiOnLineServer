package gestionedatabase;

import converter.ConverterFactory;
import converter.ModelToProtoAppello;
import converter.ModelToProtoDomanda;
import support.Notificatore;
import exception.AppelloAlreadyStartedException;
import exception.AppelloNotFoundException;
import exception.OperationDBException;
import exception.UtenteAlreadyRegisteredException;
import model.Appello;
import model.Domanda;
import model.Studente;
import proto.Remotemethod;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public final class Handler implements HandlerDB{ //service

    ConverterFactory af = ConverterFactory.FACTORY;

    Repository r = new Repository();

    Map<Appello,List<Domanda>> domandeAppello = new HashMap<>();
    Map<Appello, Notificatore> notificatoreMap = new HashMap<>();

    ScheduledExecutorService esecutore;


    public Handler(){
        esecutore = Executors.newScheduledThreadPool(30);
    }

    public String addStudent(Remotemethod.Studente studente){
        int idAppello = studente.getIdAppello();
        String matricola = studente.getMatricola();
        String codFiscale = studente.getCodFiscale();

        //Verifico che l'appello per cui si vuole registrare sia valido
        List<Appello> p = r.cercaAppello(idAppello);
        if(p.size() != 1 || p.get(0).getData().isBefore(LocalDate.now()) || p.get(0).getOra().isBefore(LocalTime.now()))
            throw new AppelloNotFoundException();

        //Verifico che lo studente non sia gia' presente nel database
        List<Studente> listaS = r.cercaStudente(matricola,codFiscale);
        if( ! listaS.isEmpty()) //posso aggiungere lo studente sul db
            throw new UtenteAlreadyRegisteredException(); // -> chain of responsibility

        Studente s = r.aggiungiUtente(studente);

        if(s == null)
            throw new OperationDBException();

        return s.getCodiceappello();
    }



    public static void main(String[] args){
        Remotemethod.Studente s = Remotemethod.Studente.newBuilder().setCodFiscale("23ff").setIdAppello(2).setMatricola("fef3").build();
        Handler e = new Handler();
        e.addStudent(s);
        System.out.println();
    }

    public List<Remotemethod.Appello> caricaAppelli(){
        List<Appello> appelli = r.caricaAppelli();
        List<Remotemethod.Appello> result = new LinkedList<>();
        ModelToProtoAppello conv = (ModelToProtoAppello) af.createConverterModel(Appello.class);
        for(Appello ap : appelli)
            result.add(conv.convert(ap));
        //Remotemethod.ListaAppelli output = Remotemethod.ListaAppelli.newBuilder().addAllAppelli(result).build();
        return result;
    }

    @Override
    public boolean partecipaEsame(Remotemethod.pRequest richiesta) {
        String codiceAppello = richiesta.getCodApello().toString();
        List<Appello> appello = r.ottieniAppello(codiceAppello);
        if(appello.isEmpty())
            throw new AppelloNotFoundException("Codice passato non valido");

        Appello p = appello.get(0);
        if(p.getData().isAfter(LocalDate.now()) || p.getOra().plusMinutes(30).isAfter(LocalTime.now()))
            throw new AppelloAlreadyStartedException();

        if(domandeAppello.get(p) == null){
            List<Domanda> domande = r.ottieniDomande(p);
            domandeAppello.put(p, domande);
            List<proto.Remotemethod.Domanda> listaDomande = new LinkedList<>();
            ModelToProtoDomanda conv = (ModelToProtoDomanda) af.createConverterModel(Domanda.class);
            for(Domanda d : domande)
                listaDomande.add(conv.convert(d));
            Notificatore notificatore = new Notificatore(listaDomande);
            //esecutore.schedule(notificatore,)
        }

        return false;
    }



}
