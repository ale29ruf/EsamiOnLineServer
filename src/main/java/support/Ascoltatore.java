package support;


import proto.Remotemethod;

import java.util.List;

/**
 * Interfaccia dei client (observer).
 */
public interface Ascoltatore {

    void aggiorna(List<Remotemethod.Domanda> listaDomande);
}
