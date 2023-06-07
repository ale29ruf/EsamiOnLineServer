package gestionedatabase;

import proto.Remotemethod;

import java.util.List;

public interface HandlerDB {
    List<Remotemethod.Appello> caricaAppelli();

    String addStudent(Remotemethod.Studente studente);
}
