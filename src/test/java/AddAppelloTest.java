import gestionedatabase.Repository;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import model.Appello;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import proto.Remotemethod;
import proto.SenderGrpc;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddAppelloTest {

    static String hostname = "localhost";
    static int port = 8999;
    static ManagedChannel channel;
    static SenderGrpc.SenderBlockingStub stub;


    @BeforeAll
    public static void initialize(){
        Starter s = new Starter();
        s.inizializza(false);

        channel = ManagedChannelBuilder.forAddress(hostname, port).usePlaintext().build();
        stub = SenderGrpc.newBlockingStub(channel);
    }

    /**
     * Verifico che l'aggiunta di un nuovo appello avvenga correttamente
     */
    @Test
    public void addAppelloVerify(){
        Remotemethod.ListaAppelli appelli = stub.caricaAppelli(Remotemethod.Info.newBuilder().build());
        Appello p = new Appello("Prova", Calendar.getInstance(), "120");
        Repository.REPOSITORY.aggiungiAppelloCompleto(p,new LinkedList<>(),new HashMap<>(),new HashMap<>());

        Remotemethod.ListaAppelli appelli2 = stub.caricaAppelli(Remotemethod.Info.newBuilder().build());

        List<Remotemethod.Appello> lista1 = appelli.getAppelliList();
        List<Remotemethod.Appello> lista2 = appelli2.getAppelliList();

        assertEquals(lista1.size() + 1, lista2.size());

    }

    @AfterAll
    public static void closeStub(){
        if (channel != null && !channel.isShutdown())
            channel.shutdown();
    }
}
