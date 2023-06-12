package converter;

import model.Domanda;
import model.Models;
import model.Scelte;
import proto.Remotemethod;

import java.util.LinkedList;
import java.util.List;

public class ModelToProtoDomanda implements Convertitore{

    public Remotemethod.Domanda convert(Models model){
        if(! model.isDomanda()){
            throw new ClassCastException("Tipo passato non supportato per AbstractFactory");
        }
        Domanda d = (Domanda) model;

        List<Scelte> scelte = d.getScelte();
        List<Remotemethod.Info> scelteProto = new LinkedList<>();
        for(Scelte s : scelte){
            Remotemethod.Info info = Remotemethod.Info.newBuilder().setTesto(s.getTesto()).build();
            scelteProto.add(info);
        }

        Remotemethod.ListaScelte listaScelte = Remotemethod.ListaScelte.newBuilder().addAllScelta(scelteProto).build();
        return Remotemethod.Domanda.newBuilder().setTesto(d.getTesto()).setScelte(listaScelte).build();
    }
}
