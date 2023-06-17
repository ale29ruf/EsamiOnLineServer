package support;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import proto.Remotemethod;
import proto.SenderGrpc;

import java.util.List;

public class Client implements Ascoltatore{

    SenderGrpc.SenderBlockingStub stub;
    ManagedChannel channel;

    public Client(String hostname, int port){
        channel = ManagedChannelBuilder.forAddress(hostname, port).usePlaintext().build();
        System.out.println("Channel con il nuovo client avente hostname "+hostname+" e porta "+port+" creato");
        stub = SenderGrpc.newBlockingStub(channel);
    }

    @Override
    public void aggiorna(List<Remotemethod.Domanda> listaDomande) {
        stub.inviaDomande(Remotemethod.ListaDomande.newBuilder().addAllDomande(listaDomande).build());
        System.out.println("Domande inviate");
        channel.shutdown();
    }
}
