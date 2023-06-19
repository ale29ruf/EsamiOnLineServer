package converter;

import model.Models;
import model.Risposta;
import proto.Remotemethod;

public class ModelToProtoRisposta implements Convertitore{

    public Remotemethod.Risposta convert(Models model){
        if(!model.isRisposta()){
            throw new ClassCastException("Tipo passato non supportato per AbstractFactory");
        }
        Risposta r = (Risposta) model;
        return Remotemethod.Risposta.newBuilder().setIdDomanda(r.getIddomanda()).setIdScelta(r.getScelta()).setTesto(r.getTestoScelta()).build();
    }
}
