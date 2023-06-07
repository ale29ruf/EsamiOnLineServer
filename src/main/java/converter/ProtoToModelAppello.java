package converter;

import model.Appello;
import model.Models;
import proto.Remotemethod;

import java.time.LocalDate;
import java.time.LocalTime;

public class ProtoToModelAppello implements Convertitore{

    public Models convert(Remotemethod.Appello appello){
        Appello result = new Appello();
        result.setId(appello.getId());
        result.setData(LocalDate.parse(appello.getData()));
        result.setOra(LocalTime.parse(appello.getOra()));
        result.setDurata(appello.getDurata());
        return result;
    }
}
