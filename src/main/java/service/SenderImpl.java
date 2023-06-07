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
        Callable<String> task = () -> g_DB.partecipaEsame(request);
        Future<String> result = esecutore.submit(task);
        String esito;
        try{
            esito = result.get(5,TimeUnit.MINUTES); //il thread potrebbe essere messo in attesa
        }catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException("Tempo di connessione al server fallito"); //scrivere bene le eccezioni e mandarle al logger
        }

        Remotemethod.Info risposta = Remotemethod.Info.newBuilder().setTesto(esito).build();

        responseObserver.onNext(risposta);
        responseObserver.onCompleted();
    }


/*
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
