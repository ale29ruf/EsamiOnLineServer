package model;

public interface Models {
    default boolean isAppello(){ //evito runtime type identification
        return false;
    }


}
