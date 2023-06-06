package gestionedatabase;

import abstractfactory.AbstractFactory;
import abstractfactory.AppelloFactory;
import model.Appello;
import proto.Remotemethod;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.LinkedList;
import java.util.List;

public final class Handler implements HandlerDB{

    EntityManagerFactory emf;
    EntityManager em;

    AbstractFactory af = AppelloFactory.FACTORY;

    public Handler(){
        emf = Persistence.createEntityManagerFactory("MyPersistenceUnit");
        em = emf.createEntityManager();
    }

    public boolean addStudent(int appello, Remotemethod.CodiceAppello cod){
        String codice = cod.getCodice();

        return true;
    }

    public List<Remotemethod.Appello> caricaAppelli(){
        String queryString = "SELECT e FROM Appello e";
        TypedQuery<Appello> query = em.createQuery(queryString, Appello.class);
        List<Appello> appelli = query.getResultList();
        List<Remotemethod.Appello> result = new LinkedList<>();
        for(Appello ap : appelli)
            result.add((Remotemethod.Appello)af.createProto(ap));
        //Remotemethod.ListaAppelli output = Remotemethod.ListaAppelli.newBuilder().addAllAppelli(result).build();
        return result;
    }

    public List<Remotemethod.Domanda> caricaDomandeAppello(int idAppello){
        //QUERY
        return null;
    }

    public  List<Remotemethod.Risposta> ottieniRisposte(int idAppello){
        //QUERY
        return null;
    }

    public void closeHandler(){
        em.close();
        emf.close();
    }

}
