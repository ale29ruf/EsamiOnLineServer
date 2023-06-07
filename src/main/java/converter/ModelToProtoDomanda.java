package converter;

import model.Domanda;
import model.Models;
import proto.Remotemethod;

public class ModelToProtoDomanda implements Convertitore{

    public Remotemethod.Domanda convert(Models model){
        if(! model.isDomanda()){
            throw new ClassCastException("Tipo passato non supportato per AbstractFactory");
        }
        Domanda d = (Domanda) model;
        Remotemethod.Domanda output = Remotemethod.Domanda.newBuilder().setTesto(d.getTesto()).build();
        return output;
    }
}
