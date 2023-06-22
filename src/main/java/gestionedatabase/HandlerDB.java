package gestionedatabase;

import proto.Remotemethod;

import java.util.List;

public interface HandlerDB {
    List<Remotemethod.Appello> caricaAppelli();
    default void aggiornaCache(){}

    String addStudent(Remotemethod.Studente studente);

    String partecipaEsame(Remotemethod.pRequest richiesta);

    List<Remotemethod.Risposta> inviaRisposte(int idAppello);
}
