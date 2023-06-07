package converter;

import model.Appello;
import model.Models;
import model.Studente;
import proto.Remotemethod;

public class ProtoToModelStudente implements Convertitore{
    public Models convert(Remotemethod.Studente studente, Appello appello, String cod){
        Studente s = new Studente();
        s.setIdappello(appello);
        s.setCodiceappello(cod);
        s.setMatricola(studente.getMatricola());
        s.setCodfiscale(studente.getCodFiscale());
        return s;
    }
}
