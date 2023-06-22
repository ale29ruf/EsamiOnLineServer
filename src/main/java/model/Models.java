package model;

/**
 * L'interfaccia viene utilizzata per indicare tutti gli oggetti che fanno parte del mapping relazionale a oggetti.
 */
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

    default boolean isRisposta(){
        return false;
    }
}
