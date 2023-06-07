package abstractfactory;

import model.Appello;
import model.Models;
import model.Studente;
import proto.Remotemethod;

import java.time.LocalDate;
import java.time.LocalTime;

public enum AppelloFactory implements AbstractFactory{

    FACTORY; // -> singleton


    @Override
    public Models createModel(Remotemethod.AppelloOrBuilder appello) {
        Appello result = new Appello();
        result.setId(appello.getId());
        result.setData(LocalDate.parse(appello.getData()));
        result.setOra(LocalTime.parse(appello.getOra()));
        result.setDurata(appello.getDurata());
        return result;
    }

    @Override
    public Remotemethod.AppelloOrBuilder createProto(Models model) {
        if(! model.isAppello()){
            throw new ClassCastException("Tipo passato non supportato per AbstractFactory");
        }
        Appello appello = (Appello)model;
        return Remotemethod.Appello.newBuilder().setData(appello.getData().toString()).setId(appello.getId()).setDurata(appello.getDurata()).setOra(appello.getOra().toString()).build();
    }

    @Override
    public Models createModel(Remotemethod.StudenteOrBuilder studente, Appello appello, String cod) {
        Studente s = new Studente();
        s.setIdappello(appello);
        s.setCodiceappello(cod);
        s.setMatricola(studente.getMatricola());
        s.setCodfiscale(studente.getCodFiscale());
        return s;
    }
}
