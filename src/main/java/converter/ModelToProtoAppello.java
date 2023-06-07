package converter;

import model.Appello;
import model.Models;
import proto.Remotemethod;

public class ModelToProtoAppello implements Convertitore{

    public Remotemethod.Appello convert(Models model){
        if(! model.isAppello()){
            throw new ClassCastException("Tipo passato non supportato per AbstractFactory");
        }
        Appello appello = (Appello)model;
        return Remotemethod.Appello.newBuilder().setId(appello.getId()).setDurata(appello.getDurata()).setOra(appello.getOra().toString()).build();
    }
}
