package support;


import proto.Remotemethod;

import java.util.List;

public interface Ascoltatore {

    void aggiorna(List<Remotemethod.Domanda> listaDomande);
}
