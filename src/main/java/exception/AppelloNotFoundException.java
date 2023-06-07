package exception;

public class AppelloNotFoundException extends RuntimeException{

    public AppelloNotFoundException(){

    }

    public AppelloNotFoundException(String msg){
        super(msg);
    }
}
