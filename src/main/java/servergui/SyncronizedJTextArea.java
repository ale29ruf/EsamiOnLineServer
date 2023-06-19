package servergui;

import javax.swing.*;

public class SyncronizedJTextArea extends JTextArea {

    public synchronized void segnala(String text){
        append(text);
    }
}
