package abstractfactory;

import model.Models;
import proto.Remotemethod;

public interface AbstractFactory {
    Models createModel(Remotemethod.AppelloOrBuilder appello);
    Remotemethod.AppelloOrBuilder createProto(Models model);
}
