package service;


import com.sun.org.apache.xpath.internal.operations.Bool;
import gestionedatabase.HandlerDB;
import io.grpc.stub.StreamObserver;
import proto.Remotemethod;
import proto.SenderGrpc;

import java.util.List;
import java.util.concurrent.*;

public class SenderImpl extends SenderGrpc.SenderImplBase implements ServiceIF{

    ScheduledExecutorService esecutore;
    HandlerDB g_DB;


    public SenderImpl(HandlerDB g_DB){
        this.g_DB = g_DB;
        esecutore = Executors.newScheduledThreadPool(30);
    }

    @Override
    public void caricaAppelli(proto.Remotemethod.Info request,
                              io.grpc.stub.StreamObserver<proto.Remotemethod.ListaAppelli> responseObserver) {
        List<Remotemethod.Appello> appelli;
        Callable<List<Remotemethod.Appello>> task = () -> g_DB.caricaAppelli();
        Future<List<Remotemethod.Appello>> result = esecutore.submit(task);
        try {
            appelli = result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        Remotemethod.ListaAppelli response = Remotemethod.ListaAppelli.newBuilder().addAllAppelli(appelli).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


    @Override
    public void registraStudente(proto.Remotemethod.Studente request,
                                 io.grpc.stub.StreamObserver<proto.Remotemethod.CodiceAppello> responseObserver) {

        Callable<String> task = () -> g_DB.addStudent(request);
        Future<String> result = esecutore.submit(task);
        String cod;
        try{
            cod = result.get(20,TimeUnit.SECONDS);
        }catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException("Tempo di connessione al server fallito"); //scrivere bene le eccezioni e mandarle al logger
        }

        Remotemethod.CodiceAppello codiceAppello = Remotemethod.CodiceAppello.newBuilder().setCodice(cod).build();

        responseObserver.onNext(codiceAppello);
        responseObserver.onCompleted();
    }

    @Override
    public void partecipaEsame(Remotemethod.pRequest request, StreamObserver<Remotemethod.Info> responseObserver) {
        Callable<Boolean> task = () -> g_DB.partecipaEsame(request);
        Future<Boolean> result = esecutore.submit(task);
        boolean esito;
        try{
            esito = result.get(20,TimeUnit.SECONDS);
        }catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException("Tempo di connessione al server fallito"); //scrivere bene le eccezioni e mandarle al logger
        }

        String testo;
        if(esito){
            testo = "Attendere inizio prova.";
            // avviare thread
        } else {
            testo = "Tempo di partecipazione alla prova scaduto.";
        }
        Remotemethod.Info risposta = Remotemethod.Info.newBuilder().setTesto(testo).build();

        responseObserver.onNext(risposta);
        responseObserver.onCompleted();
    }

    /*
    public void partecipaEsame(proto.Remotemethod.Info request,
                               io.grpc.stub.StreamObserver<proto.Remotemethod.ListaDomande> responseObserver) {
        List<Remotemethod.Domanda> domande = g_DB.caricaDomandeAppello(request.getComment());
        Remotemethod.ListaDomande response = Remotemethod.ListaDomande.newBuilder().addAllDomande(domande).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void inviaRisposte(proto.Remotemethod.RispostaAppello request,
                              io.grpc.stub.StreamObserver<proto.Remotemethod.Modulo> responseObserver) {
        List<Remotemethod.Risposta> rispostaList = g_DB.ottieniRisposte(Integer.parseInt(request.getIdAppello()));
        Remotemethod.ListaRisposte risposte = Remotemethod.ListaRisposte.newBuilder().addAllRisposte(rispostaList).build();
        int punteggio = calcolaPunteggio(request.getListaRisposte().getRisposteList(), rispostaList);
        Remotemethod.Modulo module = Remotemethod.Modulo.newBuilder().setIdAppello(request.getIdAppello()).setListaRisposte(risposte).setPunteggio(punteggio).build();
        responseObserver.onNext(module);
        responseObserver.onCompleted();
    }

    private int calcolaPunteggio(List<Remotemethod.Risposta> risposteList, List<Remotemethod.Risposta> rispostaList) {
        int punteggio = 0;
        for (int i=0; i<risposteList.size(); i++){
            int risposta = risposteList.get(i).getRisposta();
            if (risposta == -1) //risposta non data
                punteggio += -1;
            else if (risposteList.get(i).getRisposta() == rispostaList.get(i).getRisposta())
                punteggio += 3;
        }
        return punteggio;
    }

     */
}
