package converter;

import model.Appello;
import model.Models;
import proto.Remotemethod;

import java.util.Calendar;

public class ModelToProtoAppello implements Convertitore{

    public Remotemethod.Appello convert(Models model){
        if(! model.isAppello()){
            throw new ClassCastException("Tipo passato non supportato per AbstractFactory");
        }
        Appello appello = (Appello)model;
        Calendar dataOra = appello.getOra();
        String riassunto = ""+ dataOra.get(Calendar.DAY_OF_MONTH)+"-"+ dataOra.get(Calendar.MONTH) + "-" + dataOra.get(Calendar.YEAR)
                + "    " + dataOra.get(Calendar.HOUR_OF_DAY) + ":" + dataOra.get(Calendar.MINUTE) + ":" + dataOra.get(Calendar.SECOND);
        return Remotemethod.Appello.newBuilder().setId(appello.getId()).setDurata(appello.getDurata()).setOra(riassunto).setNome(appello.getNome()).build();
    }
}
