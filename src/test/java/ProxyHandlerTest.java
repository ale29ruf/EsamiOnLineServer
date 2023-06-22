import gestionedatabase.Repository;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import model.Appello;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import proto.Remotemethod;
import proto.SenderGrpc;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProxyHandlerTest {
    static String hostname = "localhost";
    static int port = 8999;
    static ManagedChannel channel;
    static SenderGrpc.SenderBlockingStub stub;


    @BeforeAll
    public static void initialize(){
        channel = ManagedChannelBuilder.forAddress(hostname, port).usePlaintext().build();
        stub = SenderGrpc.newBlockingStub(channel);
    }


    @Disabled("Test disabilitato per il momento")
    @Test
    public void addStudentVerify(){

        Remotemethod.Studente studente1 = Remotemethod.Studente.newBuilder().setIdAppello(12).setMatricola("132323")
                .setCodFiscale("012445a667782123").build();
        Remotemethod.CodiceAppello codiceAppello = stub.registraStudente(studente1);
        assertEquals(32, codiceAppello.getCodice().length());

        Remotemethod.CodiceAppello codiceAppelloError = stub.registraStudente(studente1);
        assertEquals(codiceAppelloError.getCodice(),"ERRORE: Utente già registrato");

    }


    @Test
    public void addAppelloVerify(){
        Remotemethod.ListaAppelli appelli = stub.caricaAppelli(Remotemethod.Info.newBuilder().build());
        Appello p = new Appello("Prova2", Calendar.getInstance(), "120");

        String domanda1 = "Capitale d'Italia?";
        List<String> listaDomande = new LinkedList<>();
        listaDomande.add(domanda1);

        Map<Integer,List<String>> scelte = new HashMap<>();
        List<String> listaScelte = new LinkedList<>();
        listaScelte.add("Roma");
        listaScelte.add("Berlino");
        scelte.put(0,listaScelte);

        Map<Integer,String> risposte = new HashMap<>();
        risposte.put(0,"Roma");

        Repository.REPOSITORY.aggiungiAppelloCompleto(p,listaDomande,scelte,risposte);

        Remotemethod.ListaAppelli appelli2 = stub.caricaAppelli(Remotemethod.Info.newBuilder().build());

        List<Remotemethod.Appello> lista1 = appelli.getAppelliList();
        List<Remotemethod.Appello> lista2 = appelli2.getAppelliList();

        assertEquals(lista1.size() + 1, lista2.size());

    }

    @BeforeAll
    public static void closeStub(){
        if (channel != null && !channel.isShutdown())
            channel.shutdown();
    }

}
