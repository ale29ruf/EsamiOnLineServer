package exception;

public class UtenteAlreadyRegisteredException extends RuntimeException{

    public UtenteAlreadyRegisteredException(){

    }

    public UtenteAlreadyRegisteredException(String msg){
        super(msg);
    }
}
