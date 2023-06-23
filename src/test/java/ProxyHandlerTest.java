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

public class ProxyHandlerTest {
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
     * Verifico che l'aggiunta di un utente già esistente non possa essere effettuata
     */
    @Test
    public void addStudentVerify(){

        Remotemethod.Studente studente = Remotemethod.Studente.newBuilder().setIdAppello(12).setMatricola("022323")
                .setCodFiscale("012445a6f77821d3").build();
        Remotemethod.CodiceAppello codiceAppello = stub.registraStudente(studente);
        assertEquals(32, codiceAppello.getCodice().length());

        Remotemethod.CodiceAppello codiceAppelloError = stub.registraStudente(studente);
        assertEquals(codiceAppelloError.getCodice(),"ERRORE: Utente già registrato");

    }


    /**
     * Verifico che l'aggiunta di un nuovo appello avvenga correttamente
     */
    @Test
    public void addAppelloVerify(){
        Remotemethod.ListaAppelli appelli = stub.caricaAppelli(Remotemethod.Info.newBuilder().build());
        Appello p = new Appello("Prova", Calendar.getInstance(), "120");

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

    @AfterAll
    public static void closeStub(){
        if (channel != null && !channel.isShutdown())
            channel.shutdown();
    }

}
