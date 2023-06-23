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

/**
 * La seguente classe si occupa di testare le funzionalità "registraStudente()".
 */

public class AddStudentTest {
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
     * Verifico che il server rispondi correttamente nel caso in cui l'utente cerchi di prenotarsi per un appello non esistente.
     */
    @Test
    public void appelloNotFoundVerify(){
        Remotemethod.Studente studente = Remotemethod.Studente.newBuilder().setIdAppello(0).setMatricola("022323")
                .setCodFiscale("012445a6f77821d3").build();
        Remotemethod.CodiceAppello codiceAppelloError = stub.registraStudente(studente);
        assertEquals(codiceAppelloError.getCodice(),"ERRORE: Nessun appello trovato.");
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
     * Il seguente metodo si occupa di verificare la generazione dell'eccezione AppelloNotFoundException ma nel caso in cui il tempo a
     * disposizione per potersi prenotare non è sufficiente.
     */
    @Test
    public void appelloNotFoundInTime(){
        Calendar dateP = Calendar.getInstance();
        dateP.add(Calendar.MINUTE,4); //ricordiamo che e' possibile prenotarsi per un appello fino a 5 minuti prima dell'inizio
        Appello p = new Appello("Prova", Calendar.getInstance(), "120");
        Repository.REPOSITORY.aggiungiAppelloCompleto(p,new LinkedList<>(),new HashMap<>(),new HashMap<>());

        int idAppelloJustAdded = Repository.REPOSITORY.cercaAppelloPerNome(p.getNome()).get(0).getId();

        //Aggiungiamo un nuovo utente per l'appello creato
        Remotemethod.Studente studente = Remotemethod.Studente.newBuilder().setIdAppello(idAppelloJustAdded).setMatricola("022323")
                .setCodFiscale("012445a6f77821d3").build();
        Remotemethod.CodiceAppello codiceAppelloError = stub.registraStudente(studente);

        assertEquals(codiceAppelloError.getCodice(),"ERRORE: Nessun appello trovato.");
    }
    @AfterAll
    public static void closeStub(){
        if (channel != null && !channel.isShutdown())
            channel.shutdown();
    }

}
