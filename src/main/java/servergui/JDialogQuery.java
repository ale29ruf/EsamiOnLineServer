package servergui;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class JDialogQuery extends JDialog {

    List<String> domande = new LinkedList<>();
    int numDomanda = 1;
    Map<Integer,List<String>> scelteDomanda = new HashMap<>();
    Map<Integer,String> rispostaDomanda = new HashMap<>();

    JLabel etichetta;
    JTextField campo;
    int totDomande;

    public JDialogQuery(JFrame f,int totDomande) {
        super(f, "Finestra di aggiunta appello", true);

        this.totDomande = totDomande;

        JPanel pannello = new JPanel();
        etichetta = new JLabel("Domanda n.1: ");
        campo = new JTextField(10);


        JPanel bottoni = new JPanel();
        JToolBar raccoglitore = new JToolBar();

        JButton prossimaDomanda = new JButton("Salva");
        prossimaDomanda.setVisible(false);


        JButton prossimaScelta = new JButton("Conferma e procedi");
        prossimaScelta.setVisible(false);
        prossimaScelta.addActionListener( e -> {
            if(caricaSceltaSuccessiva())
                prossimaDomanda.setVisible(true);

        });

        JButton confermaDomanda = new JButton("Conferma");
        confermaDomanda.addActionListener( e -> {
            if(caricaScelte()){
                confermaDomanda.setVisible(false);
                prossimaScelta.setVisible(true);
            }
        });

        prossimaDomanda.addActionListener( e -> {
            if(caricaDomandaSuccessiva()){
                prossimaDomanda.setVisible(false);
                prossimaScelta.setVisible(false);
                confermaDomanda.setVisible(true);
            } else {
                dispose();
            }
        });


        raccoglitore.add(confermaDomanda);
        raccoglitore.add(prossimaScelta);
        raccoglitore.add(prossimaDomanda);

        bottoni.add(raccoglitore);

        pannello.add(etichetta);
        pannello.add(campo);

        JPanel mainPanel = new JPanel();
        mainPanel.add(pannello, BorderLayout.CENTER);
        mainPanel.add(bottoni, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setSize(500, 200);
        setLocationRelativeTo(f);

    }

    private boolean caricaDomandaSuccessiva() {
        String rispostaDef = "";
        while(rispostaDef.equals("")){
            String risposta = JOptionPane.showInputDialog(null, "Inserisci risposta corretta:");
            if(risposta != null){
                for(String scelta : scelteDomanda.get(numDomanda))
                    if(scelta.equalsIgnoreCase(risposta)) {
                        rispostaDef = scelta;
                    }
                if(rispostaDef.equals("")) JOptionPane.showMessageDialog(null, "Risposta non presente tra le scelte precedenti", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
        rispostaDomanda.put(numDomanda,rispostaDef);
        if(numDomanda < totDomande){
            numDomanda++;
            etichetta.setText("Domanda n."+ numDomanda +": ");
            campo.setText("");
            return true;
        }
        System.out.println("Salvato nuovo appello nel db");
        return false;
    }

    private boolean caricaSceltaSuccessiva() {
        if(campo.getText().length() == 0){
            JOptionPane.showMessageDialog(null, "Campo vuoto", "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        String testoScelta = campo.getText();
        scelteDomanda.get(numDomanda).add(testoScelta);
        etichetta.setText("Scelta n."+ (scelteDomanda.get(numDomanda).size()+1) +": ");
        campo.setText("");
        return true;
    }

    private boolean caricaScelte() {
        if(campo.getText().length() == 0){
            JOptionPane.showMessageDialog(null, "Campo vuoto", "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        String testoDomanda = campo.getText();
        domande.add(testoDomanda);
        scelteDomanda.put(numDomanda,new LinkedList<>());
        etichetta.setText ("Scelta n.1: ");
        campo.setText("");

        return true;
    }

}
