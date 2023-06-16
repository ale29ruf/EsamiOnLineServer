package converter;

import model.Domanda;
import model.Models;
import model.Scelta;
import proto.Remotemethod;

import java.util.LinkedList;
import java.util.List;

public class ModelToProtoDomanda implements Convertitore{

    public Remotemethod.Domanda convert(Models model){
        if(! model.isDomanda()){
            throw new ClassCastException("Tipo passato non supportato per AbstractFactory");
        }
        Domanda d = (Domanda) model;

        List<Scelta> scelte = d.getScelte();
        List<Remotemethod.Scelta> scelteProto = new LinkedList<>();
        for(Scelta s : scelte){
            Remotemethod.Scelta scelta = Remotemethod.Scelta.newBuilder().setId(s.getId())
                    .setTesto(s.getTesto()).build();
            scelteProto.add(scelta);
        }

        Remotemethod.ListaScelte listaScelte = Remotemethod.ListaScelte.newBuilder().addAllScelte(scelteProto).build();
        return Remotemethod.Domanda.newBuilder().setTesto(d.getTesto()).setScelte(listaScelte).setId(d.getId()).build();
    }
}
