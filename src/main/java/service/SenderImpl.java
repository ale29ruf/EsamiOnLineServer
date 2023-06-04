package service;


import proto.SenderGrpc;

public class SenderImpl extends SenderGrpc.SenderImplBase {

    @Override
    public void caricaAppelli(proto.Remotemethod.Info request,
                              io.grpc.stub.StreamObserver<proto.Remotemethod.ListaAppelli> responseObserver) {

    }

    @Override
    public void registraStudente(proto.Remotemethod.Studente request,
                                 io.grpc.stub.StreamObserver<proto.Remotemethod.CodiceAppello> responseObserver) {

    }

    @Override
    public void caricaDomandeAppello(proto.Remotemethod.Info request,
                                     io.grpc.stub.StreamObserver<proto.Remotemethod.ListaDomande> responseObserver) {

    }

    @Override
    public void inviaRisposte(proto.Remotemethod.RispostaAppello request,
                              io.grpc.stub.StreamObserver<proto.Remotemethod.Modulo> responseObserver) {
        
    }
}
