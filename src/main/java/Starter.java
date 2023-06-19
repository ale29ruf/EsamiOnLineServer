import converter.*;
import gestionedatabase.Handler;
import gestionedatabase.HandlerDB;
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

import javax.swing.*;
import java.io.IOException;

public class Starter {
    public static void main(String[] args){
        ConverterFactory.FACTORY.installConverterModel(Appello.class,new ModelToProtoAppello());
        ConverterFactory.FACTORY.installConverterProto(Remotemethod.Studente.class,new ProtoToModelStudente());
        ConverterFactory.FACTORY.installConverterModel(Domanda.class, new ModelToProtoDomanda());
        ConverterFactory.FACTORY.installConverterModel(Risposta.class, new ModelToProtoRisposta());

        Interface serverInterface = new Interface();
        SyncronizedJTextArea logger = serverInterface.avvia();

        Handler gestore = new Handler();
        ProxyHandler gestoreProxy = new ProxyHandler(gestore);
        gestoreProxy.setLogger(logger);

        Server server = ServerBuilder.forPort(8999).addService(new SenderImpl(gestoreProxy)).build();
        try {
            server.start();
            System.out.println("Server started at " + server.getPort());
            server.awaitTermination(); //blocca l'esecuzione del thread corrente finché il server gRPC non viene terminato
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
