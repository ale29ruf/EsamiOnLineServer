package servergui;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class JDialogQuery extends JDialog {

    List<String> domande = new LinkedList<>();
    int numDomanda = -1;
    Map<Integer,List<String>> scelteDomanda = new HashMap<>();
    Map<Integer,String> rispostaDomanda = new HashMap<>();

    JPanel pannello;
    JLabel etichetta;
    JTextField campo;
    JButton confermaDomanda;
    int totDomande;

    public JDialogQuery(JFrame f,int totDomande) {
        super(f, "Finestra di aggiunta appello", true);

        this.totDomande = totDomande;

        pannello = new JPanel();
        etichetta = new JLabel("Domanda n.1: ");
        campo = new JTextField();


        JPanel bottoni = new JPanel();
        JToolBar raccoglitore = new JToolBar();

        JButton prossimaDomanda = new JButton("Conferma e salva");
        prossimaDomanda.setVisible(false);
        prossimaDomanda.addActionListener( e -> {
            caricaDomandaSuccessiva();
            prossimaDomanda.setVisible(false);
        });

        JButton prossimaScelta = new JButton("Conferma e procedi");
        prossimaScelta.setVisible(false);
        prossimaScelta.addActionListener( e -> {
            prossimaDomanda.setVisible(true);
            caricaSceltaSuccessiva();
        });



        confermaDomanda = new JButton("Conferma");
        confermaDomanda.addActionListener( e -> {
            caricaScelte();
            confermaDomanda.setVisible(false);
            prossimaScelta.setVisible(true);
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
        setSize(500, 300);
        setLocationRelativeTo(f);

    }

    private void caricaDomandaSuccessiva() {
        boolean stop = false;
        String risposta = "";
        while(!stop){
            risposta = JOptionPane.showInputDialog(null, "Inserisci risposta corretta:");
            if( !scelteDomanda.get(numDomanda).contains(risposta)){
                JOptionPane.showMessageDialog(null, "Risposta non presente tra le scelte precedenti", "Errore", JOptionPane.ERROR_MESSAGE);
            } else {
                stop = true;
            }
        }
        rispostaDomanda.put(numDomanda,risposta);
        if(numDomanda < totDomande){
            numDomanda++;
            etichetta = new JLabel("Domanda n."+ numDomanda +": ");
            campo = new JTextField();
            confermaDomanda.setVisible(true);
            pannello.revalidate();
            pannello.repaint();
        }
    }

    private void caricaSceltaSuccessiva() {
        if(campo.getText().length() == 0){
            JOptionPane.showMessageDialog(null, "Campo vuoto", "Errore", JOptionPane.ERROR_MESSAGE);

        } else {
            String testoScelta = campo.getText();
            scelteDomanda.get(numDomanda).add(testoScelta);
            etichetta = new JLabel("Scelta n."+ (scelteDomanda.get(numDomanda).size()+1) +": ");
            campo = new JTextField();
            pannello.revalidate();
            pannello.repaint();
        }
    }

    private void caricaScelte() {
        if(campo.getText().length() == 0){
            JOptionPane.showMessageDialog(null, "Campo vuoto", "Errore", JOptionPane.ERROR_MESSAGE);

        } else {
            String testoDomanda = campo.getText();
            domande.add(testoDomanda);
            numDomanda++;
            scelteDomanda.put(numDomanda,new LinkedList<>());
            etichetta = new JLabel("Scelta n.1: ");
            campo = new JTextField();
            pannello.revalidate();
            pannello.repaint();

        }
    }

}
