package service;


import gestionedatabase.HandlerDB;
import proto.Remotemethod;
import proto.SenderGrpc;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

public class SenderImpl extends SenderGrpc.SenderImplBase implements ServiceIF{

    ScheduledExecutorService esecutore;
    HandlerDB g_DB;
    final UUID uuid = UUID.randomUUID();

    public SenderImpl(HandlerDB g_DB){
        this.g_DB = g_DB;
        esecutore = Executors.newScheduledThreadPool(30);
    }

    @Override
    public void caricaAppelli(proto.Remotemethod.Info request,
                              io.grpc.stub.StreamObserver<proto.Remotemethod.ListaAppelli> responseObserver) {
        List<Remotemethod.Appello> appelli;
        Callable<List<Remotemethod.Appello>> task = () -> {
            List<Remotemethod.Appello> appelloList = g_DB.caricaAppelli();
            return appelloList;
        };
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

    /*
    @Override
    public void registraStudente(proto.Remotemethod.Studente request,
                                 io.grpc.stub.StreamObserver<proto.Remotemethod.CodiceAppello> responseObserver) {
        Remotemethod.CodiceAppello cod = Remotemethod.CodiceAppello.newBuilder().setCodice(uuid.toString().replace("-", "")).build();
        if( ! g_DB.addStudent(request.getIdAppello(),cod))
            cod = Remotemethod.CodiceAppello.newBuilder().setCodice("Errore registrazione").build();

        responseObserver.onNext(cod);
        responseObserver.onCompleted();
    }

    @Override
    public void caricaDomandeAppello(proto.Remotemethod.Info request,
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
