package servergui;

import gestionedatabase.Repository;
import model.Appello;

import javax.swing.*;
import java.util.Calendar;
import java.util.List;

/**
 * Classe utilizzata per raccogliere le informazioni di base di un appello.
 */
public class PannelloAggiuntaAppello extends JPanel {

    private JTextField nomeField;
    private JTextField durataField;
    private JTextField giornoField;
    private JTextField oraField;
    private JTextField numDomandeField;
    private JButton registraAppello;
    Repository r = Repository.REPOSITORY;

    public PannelloAggiuntaAppello(){
        //setLayout(new GridLayout(1, 8, 5, 5)); // Layout con 4 righe, 2 colonne, spaziatura tra i componenti

        JLabel nome = new JLabel("Nome: ");
        nomeField = new JTextField(20);

        JLabel durata = new JLabel("Durata(min): ");
        durataField = new JTextField(3);

        JLabel giorno = new JLabel("Giorno: ");
        giornoField = new JTextField(8);

        JLabel ora = new JLabel("Ora: ");
        oraField = new JTextField(8);

        JLabel numDomande = new JLabel("Numero domande: ");
        numDomandeField = new JTextField(2);

        registraAppello = new JButton("Aggiungi");
        registraAppello.addActionListener( e -> {
            processaAppello();
        });

        add(nome);
        add(nomeField);
        add(durata);
        add(durataField);
        add(giorno);
        add(giornoField);
        add(ora);
        add(oraField);
        add(numDomande);
        add(numDomandeField);
        add(registraAppello);
    }

    private void processaAppello(){
        if(nomeField.getText().length() == 0 || durataField.getText().length() == 0 ||
        giornoField.getText().length() == 0 || oraField.getText().length() == 0) {
            notificaErroreCampi(null);
            return;
        }

        List<Appello> listaAppello = r.cercaAppelloPerNome(nomeField.getText());
        if(!listaAppello.isEmpty()){
            notificaErroreCampi("Appello già esistente");
            return;
        }

        String[] giornoParts = giornoField.getText().split("[:/-]");
        if(giornoParts.length != 3){
            notificaErroreCampi("Campo giorno non specificato correttamente");
            return;
        }

        String[] oraParts = oraField.getText().split("[:/-]");
        if(oraParts.length != 3){
            notificaErroreCampi("Campo ora non specificato correttamente");
            return;
        }

        Calendar dataOra = Calendar.getInstance();
        dataOra.set(Calendar.YEAR, Integer.parseInt(giornoParts[2]));
        dataOra.set(Calendar.MONTH, Integer.parseInt(giornoParts[1]));
        dataOra.set(Calendar.DAY_OF_MONTH, Integer.parseInt(giornoParts[0]));
        dataOra.set(Calendar.HOUR_OF_DAY, Integer.parseInt(oraParts[0]));
        dataOra.set(Calendar.MINUTE, Integer.parseInt(oraParts[1]));
        dataOra.set(Calendar.SECOND, Integer.parseInt(oraParts[2]));

        int numDomande;
        if(numDomandeField.getText().length() == 0){
            notificaErroreCampi("Campo Numero domande non specificato correttamente");
            return;
        } else {
            numDomande = Integer.parseInt(numDomandeField.getText());
            if(numDomande == 0) {
                notificaErroreCampi("L'appello deve avere almeno una domanda");
                return;
            }
        }

        JDialogAppello jDialogAppello = new JDialogAppello((JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, this),numDomande);
        jDialogAppello.passaInfoAppello(nomeField.getText(),durataField.getText(),dataOra);
        jDialogAppello.setVisible(true);

    }


    private void notificaErroreCampi(String message){
        if(!(message == null))
            JOptionPane.showMessageDialog(null, message, "Errore", JOptionPane.ERROR_MESSAGE);
        else
            JOptionPane.showMessageDialog(null, "Campi inseriti non validi", "Errore", JOptionPane.ERROR_MESSAGE);
    }


}
