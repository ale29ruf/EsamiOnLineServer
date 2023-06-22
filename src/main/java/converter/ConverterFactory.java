package converter;

import com.google.protobuf.MessageOrBuilder;
import model.Models;

import java.util.HashMap;
import java.util.Map;

/**
 * La classe si occupa di convertire gli oggetti ottenuti dal database in oggetti da trasmettere tramite chiamata
 * a procedura remota e viceversa.
 */
public enum ConverterFactory {

    FACTORY;

    private final Map<Class<? extends Models>, Convertitore> viewMapModel = new HashMap<>();

    private final Map<Class<? extends MessageOrBuilder>, Convertitore> viewMapProto = new HashMap<>();

    public Convertitore createConverterModel( Class<? extends Models> clazz) {
        return viewMapModel.get(clazz);
    }

    public Convertitore createConverterProto(Class<? extends MessageOrBuilder> clazz) {
        return viewMapProto.get(clazz);
    }

    public void installConverterModel(Class<? extends Models> clazz, Convertitore view) {
        viewMapModel.put(clazz, view);
    }

    public void installConverterProto(Class<? extends MessageOrBuilder> clazz, Convertitore view) {
        viewMapProto.put(clazz, view);
    }

}
