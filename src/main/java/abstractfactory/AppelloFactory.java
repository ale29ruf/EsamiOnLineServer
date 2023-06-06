package abstractfactory;

import model.Appello;
import model.Models;
import proto.Remotemethod;

public enum AppelloFactory implements AbstractFactory{

    FACTORY;

    @Override
    public Models createModel(Remotemethod.AppelloOrBuilder appello) {
        Appello result = new Appello();
        result.setId(appello.getId());
        result.setData(appello.getData());
        result.setOra(appello.getOra());
        result.setDurata(appello.getDurata());
        return result;
    }

    @Override
    public Remotemethod.AppelloOrBuilder createProto(Models model) {
        if(! model.isAppello()){
            throw new RuntimeException("Tipo passato non supportato per AbstractFactory");
        }
        Appello appello = (Appello)model;
        return Remotemethod.Appello.newBuilder().setData(appello.getData()).setId(appello.getId()).setDurata(appello.getDurata()).setOra(appello.getOra()).build();
    }
}
