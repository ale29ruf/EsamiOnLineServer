package support;

import proto.Remotemethod;
import proto.SenderGrpc;

import java.util.List;

public class Client implements Ascoltatore{

    SenderGrpc.SenderBlockingStub senderBlockingStub;

    public Client(SenderGrpc.SenderBlockingStub senderBlockingStub){
        this.senderBlockingStub = senderBlockingStub;
    }

    @Override
    public void aggiorna(List<Remotemethod.Domanda> listaDomande) {
        senderBlockingStub.inviaDomande(Remotemethod.ListaDomande.newBuilder().addAllDomande(listaDomande).build());
    }
}
