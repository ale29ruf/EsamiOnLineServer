package model;

public interface Models {
    default boolean isAppello(){ //evito runtime type identification
        return false;
    }

    default boolean isDomanda(){
        return false;
    }

    default boolean isStudente() {
        return false;
    }


}
