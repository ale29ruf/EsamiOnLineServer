package service;

import proto.Remotemethod;

import java.util.List;

public final class GestoreDB {

        public boolean addStudent(int appello, Remotemethod.CodiceAppello cod){
            String codice = cod.getCodice();

            return true;
        }

        public List<Remotemethod.Appello> caricaAppelli(){
            //QUERY
            return  null;
        }

        public List<Remotemethod.Domanda> caricaDomandeAppello(int idAppello){
            //QUERY
            return null;
        }

        public  List<Remotemethod.Risposta> ottieniRisposte(int idAppello){
            //QUERY
            return null;
        }

}
