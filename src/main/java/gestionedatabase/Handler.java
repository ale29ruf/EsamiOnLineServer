package gestionedatabase;

import abstractfactory.AbstractFactory;
import abstractfactory.AppelloFactory;
import exception.AppelloNotFoundException;
import exception.OperationDBException;
import exception.UtenteAlreadyRegisteredException;
import model.Appello;
import model.Studente;
import proto.Remotemethod;

import java.util.LinkedList;
import java.util.List;

public final class Handler implements HandlerDB{ //service

    AbstractFactory af = AppelloFactory.FACTORY;

    Repository r = new Repository();

    public Handler(){
    }

    public String addStudent(Remotemethod.Studente studente){
        int idAppello = studente.getIdAppello();
        String matricola = studente.getMatricola();
        String codFiscale = studente.getCodFiscale();

        //Verifico che l'appello per cui si vuole registrare sia valido
        List<Appello> p = r.cercaAppello(idAppello);
        if(p.size() != 1)
            throw new AppelloNotFoundException();

        //Verifico che lo studente non sia gia' presente nel database
        List<Studente> listaS = r.cercaStudente(matricola,codFiscale);
        if( ! listaS.isEmpty()) //posso aggiungere lo studente sul db
            throw new UtenteAlreadyRegisteredException();

        Studente s = r.aggiungiUtente(studente);

        if(s == null)
            throw new OperationDBException();

        return s.getCodiceappello();
    }

    public static void main(String[] args){
        Remotemethod.Studente s = Remotemethod.Studente.newBuilder().setCodFiscale("23ff").setIdAppello(2).setMatricola("fef3").build();
        Handler e = new Handler();
        e.addStudent(s);
    }

    public List<Remotemethod.Appello> caricaAppelli(){
        List<Appello> appelli = r.caricaAppelli();
        List<Remotemethod.Appello> result = new LinkedList<>();
        for(Appello ap : appelli)
            result.add((Remotemethod.Appello)af.createProto(ap));
        //Remotemethod.ListaAppelli output = Remotemethod.ListaAppelli.newBuilder().addAllAppelli(result).build();
        return result;
    }

    public List<Remotemethod.Domanda> caricaDomandeAppello(int idAppello){
        //QUERY
        return null;
    }

    public  List<Remotemethod.Risposta> ottieniRisposte(int idAppello){
        //QUERY
        return null;
    }


}
