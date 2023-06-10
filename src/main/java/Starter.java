import converter.ConverterFactory;
import converter.ModelToProtoAppello;
import gestionedatabase.Handler;
import gestionedatabase.HandlerDB;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import model.Appello;
import service.SenderImpl;

import java.io.IOException;

public class Starter {
    public static void main(String[] args){
        ModelToProtoAppello conv = new ModelToProtoAppello();
        ConverterFactory.FACTORY.installConverterModel(Appello.class,conv);
        HandlerDB gestore = new Handler();
        Server server = ServerBuilder.forPort(8999).addService(new SenderImpl(gestore)).build();
        try {
            server.start();
            System.out.println("Server started at " + server.getPort());
            server.awaitTermination(); //blocca l'esecuzione del thread corrente finché il server gRPC non viene terminato
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
