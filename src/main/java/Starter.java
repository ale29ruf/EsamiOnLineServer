import converter.*;
import gestionedatabase.Handler;
import gestionedatabase.ProxyHandler;
import gestionedatabase.Repository;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import model.Appello;
import model.Domanda;
import model.Risposta;
import proto.Remotemethod;
import servergui.Interface;
import servergui.SyncronizedJTextArea;
import service.SenderImpl;

import java.io.IOException;

public class Starter {
    public static void main(String[] args){

        Starter s = new Starter();
        s.inizializza(true); //settare a true se si vuole visualizzare l'interfaccia grafica
    }

    public void inizializza(boolean conInterfaccia){
        //Installo i vari convertitori
        ConverterFactory.FACTORY.installConverterModel(Appello.class,new ModelToProtoAppello());
        ConverterFactory.FACTORY.installConverterProto(Remotemethod.Studente.class,new ProtoToModelStudente());
        ConverterFactory.FACTORY.installConverterModel(Domanda.class, new ModelToProtoDomanda());
        ConverterFactory.FACTORY.installConverterModel(Risposta.class, new ModelToProtoRisposta());

        SyncronizedJTextArea logger = new SyncronizedJTextArea();

        if(conInterfaccia) new Interface(logger);

        Handler gestore = new Handler();
        ProxyHandler gestoreProxy = new ProxyHandler(gestore);
        gestoreProxy.setLogger(logger);
        Repository.REPOSITORY.setProxy(gestoreProxy);

        Server server = ServerBuilder.forPort(8999).addService(new SenderImpl(gestoreProxy)).build();
        try {
            server.start();

            Thread thread = new Thread(() -> { //Evito che il thread principale si messa in attesa
                try {
                    server.awaitTermination(); //blocca l'esecuzione del thread corrente finché il server gRPC non viene terminato
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            thread.start(); // avvio il server

            System.out.println("Server started at " + server.getPort());

        } catch (IOException  e) {
            throw new RuntimeException(e);
        }

    }

}
