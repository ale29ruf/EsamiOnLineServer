package servergui;

import javax.swing.*;

/**
 * Classe usata per mantenere l'amministratore aggiornato in merito alle attività dei client.
 */
public class SyncronizedJTextArea extends JTextArea {

    public synchronized void segnala(String text){
        append(text);
    }
}
