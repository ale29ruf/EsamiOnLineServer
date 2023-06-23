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

public class PartecipaEsameTest {

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
     * Il metodo verifica che il server risponde bene nel caso di codice di partecipazione a un appello non valido
     */
    @Test
    public void appelloNotFound(){
        String testo = "";
        for(int i=0; i<32; i++) testo += "X";
        Remotemethod.CodiceAppello codice = Remotemethod.CodiceAppello.newBuilder().setCodice(testo).build();
        Remotemethod.pRequest request = Remotemethod.pRequest.newBuilder().setCodApello(codice).setHostaname("localhost").setPort(6969).build();

        //Inviamo la richiesta di partecipazione all'appello
        Remotemethod.Info response = stub.partecipaEsame(request);
        assertEquals(response.getTesto(),"ERRORE: Nessun appello trovato per il corrispettivo codice inviato");
    }


    /**
     * Il metodo verifica che l'utente non possa partecipare ad un appello già iniziato.
     * Ovviamente il test dipende dal parametro ritardoPartecipazioneAppello all'interno della classe Handler
     * che stabilisce il tempo in minuti oltre il quale, dall'inizio dell'appello, non è piu' possibile partecipare.
     * Per effettuare il test dobbiamo disabilitare il controllo nell'Handler sul limite di prenotazione per un appello.
     */
    @Test
    public void appelloAlreadyStarted(){
        Calendar now = Calendar.getInstance();
        now.roll(Calendar.MINUTE,-6); //ricordiamo che e' possibile partecipare ad un appello fino a 5 minuti dopo l'inizio

        Appello p = new Appello("Prova", now, "120");
        Repository.REPOSITORY.aggiungiAppelloCompleto(p,new LinkedList<>(),new HashMap<>(),new HashMap<>());

        int idAppelloJustAdded = Repository.REPOSITORY.cercaAppelloPerNome(p.getNome()).get(0).getId();

        //Aggiungiamo un nuovo studente per l'appello creato
        Remotemethod.Studente studente = Remotemethod.Studente.newBuilder().setIdAppello(idAppelloJustAdded).setMatricola("022323")
                .setCodFiscale("012445a6f77821d3").build();
        Remotemethod.CodiceAppello codiceAppello = stub.registraStudente(studente);

        assertEquals(32, codiceAppello.getCodice().length());
        //Registrazione avvenuta con successo

        Remotemethod.CodiceAppello codice = Remotemethod.CodiceAppello.newBuilder().setCodice(codiceAppello.getCodice()).build();
        Remotemethod.pRequest request = Remotemethod.pRequest.newBuilder().setCodApello(codice).setHostaname("localhost").setPort(6969).build();

        //Inviamo la richiesta di partecipazione all'appello
        Remotemethod.Info response = stub.partecipaEsame(request);

        assertEquals(response.getTesto(),"ERRORE: Appello gia' iniziato. Impossibile partecipare");
    }


    /**
     * Il seguente test verifica la possibilità di partecipare a un appello fino a 5 minuti dopo l'inizio.
     * Per effettuare il test dobbiamo disabilitare il controllo nell'Handler sul limite di prenotazione per un appello.
     */
    @Test
    public void appelloPartecipazioneConfermata(){
        Calendar now = Calendar.getInstance();
        now.roll(Calendar.MINUTE,-4); //ricordiamo che è possibile partecipare a un appello fino a 5 minuti dopo l'inizio

        Appello p = new Appello("Prova", now, "120");
        Repository.REPOSITORY.aggiungiAppelloCompleto(p,new LinkedList<>(),new HashMap<>(),new HashMap<>());

        int idAppelloJustAdded = Repository.REPOSITORY.cercaAppelloPerNome(p.getNome()).get(0).getId();

        //Aggiungiamo un nuovo studente per l'appello creato
        Remotemethod.Studente studente = Remotemethod.Studente.newBuilder().setIdAppello(idAppelloJustAdded).setMatricola("022323")
                .setCodFiscale("012445a6f77821d3").build();
        Remotemethod.CodiceAppello codiceAppello = stub.registraStudente(studente);

        assertEquals(32, codiceAppello.getCodice().length());
        //Registrazione avvenuta con successo

        Remotemethod.CodiceAppello codice = Remotemethod.CodiceAppello.newBuilder().setCodice(codiceAppello.getCodice()).build();
        Remotemethod.pRequest request = Remotemethod.pRequest.newBuilder().setCodApello(codice).setHostaname("localhost").setPort(6969).build();

        //Inviamo la richiesta di partecipazione all'appello
        Remotemethod.Info response = stub.partecipaEsame(request);

        assertEquals(response.getTesto(),idAppelloJustAdded+""); //ricordiamo che se la partecipazione va a buon fine il server restituisce l'id dell'appello
    }


    @AfterAll
    public static void closeStub(){
        if (channel != null && !channel.isShutdown())
            channel.shutdown();
    }
}
