package converter;

import model.Appello;
import model.Models;
import model.Studente;
import proto.Remotemethod;

public class ProtoToModelStudente implements Convertitore{
    public Models convert(Remotemethod.Studente studente){
        Studente s = new Studente();
        Appello p = new Appello();
        p.setId(studente.getIdAppello());
        s.setIdappello(p);
        s.setMatricola(studente.getMatricola());
        s.setCodfiscale(studente.getCodFiscale());
        return s;
    }
}
