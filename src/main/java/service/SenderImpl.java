package service;


import gestionedatabase.HandlerDB;
import io.grpc.stub.StreamObserver;
import proto.Remotemethod;
import proto.SenderGrpc;

import java.util.List;
import java.util.concurrent.*;

public final class SenderImpl extends SenderGrpc.SenderImplBase implements ServiceIF{

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
            e.printStackTrace();
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
            throw new RuntimeException(e.getCause()+"  "+e.getMessage()); //scrivere bene le eccezioni e mandarle al logger
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
            throw new RuntimeException(e.getMessage()+"\n Cause: "+e.getCause()); //scrivere bene le eccezioni e mandarle al logger
        }

        Remotemethod.Info risposta = Remotemethod.Info.newBuilder().setTesto(esito).build();

        responseObserver.onNext(risposta);
        responseObserver.onCompleted();
    }

    @Override
    public void inviaRisposte(Remotemethod.RispostaAppello request, StreamObserver<Remotemethod.Modulo> responseObserver) {
        Callable<List<Remotemethod.Risposta>> task = () -> g_DB.inviaRisposte(request.getIdAppello());
        Future<List<Remotemethod.Risposta>> result = esecutore.submit(task);
        List<Remotemethod.Risposta> risposte;
        try{
            risposte = result.get(30,TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            throw new RuntimeException(e.getMessage()); //scrivere bene le eccezioni e mandarle al logger
        }
        Remotemethod.ListaRisposte listaR = Remotemethod.ListaRisposte.newBuilder().addAllRisposte(risposte).build();
        int punteggio = calcolaPunteggio(request.getListaRisposte().getRisposteList(), risposte);
        Remotemethod.Modulo modulo = Remotemethod.Modulo.newBuilder().setIdAppello(request.getIdAppello()).setListaRisposte(listaR).setPunteggio(punteggio).build();
        responseObserver.onNext(modulo);
        responseObserver.onCompleted();
    }

    private int calcolaPunteggio(List<Remotemethod.Risposta> daVerificare, List<Remotemethod.Risposta> corrette) {
        int punteggio = 0;
        for( Remotemethod.Risposta risposta: daVerificare ) {
            for (Remotemethod.Risposta rispostaOK : corrette) {
                if (risposta.getIdDomanda() == rispostaOK.getIdDomanda())
                    if (risposta.getIdScelta() == rispostaOK.getIdScelta())
                        punteggio += 3;
                    else if (risposta.getIdScelta() == -1)
                        punteggio -= 1;
            }
        }
        return punteggio;
    }


}
