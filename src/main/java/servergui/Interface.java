package servergui;

import javax.swing.*;
import java.awt.*;

/**
 * La classe rappresenta l'interfaccia server.
 */
public class Interface {

    public Interface() {
    }

    public SyncronizedJTextArea avvia() {
        JFrame f = new JFrame("Applicazione server");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setMinimumSize(new Dimension(1000, 400));
        f.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        SyncronizedJTextArea logger = new SyncronizedJTextArea();
        logger.setEditable(false);
        logger.setSize(800,300);

        logger.setAlignmentX(Component.CENTER_ALIGNMENT);
        logger.setLineWrap(true);
        logger.setWrapStyleWord(true);

        JScrollPane jScrollPane = new JScrollPane(logger);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane.setVisible(false);

        JToolBar selettore  = new JToolBar();
        JButton visualizzaCambiamentiButton = new JButton("Abilita visualizzazione cambiamenti");
        visualizzaCambiamentiButton.addActionListener( e -> {
            logger.setVisible(true);
            jScrollPane.setVisible(true);
            aggiornaPannello(mainPanel);
        });

        JButton nonVisualizzaCambiamentiButton = new JButton("Disabilita visualizzazione cambiamenti");
        nonVisualizzaCambiamentiButton.addActionListener( e -> {
            logger.setVisible(false);
            jScrollPane.setVisible(false);
            aggiornaPannello(mainPanel);
        });


        PannelloAggiuntaAppello panelAdderAppello = new PannelloAggiuntaAppello();
        panelAdderAppello.setVisible(false);
        JButton aggiungiAppello = new JButton("Aggiungi appello");
        aggiungiAppello.addActionListener( e -> {
            panelAdderAppello.setVisible(true);
            aggiungiAppello.setVisible(false);
            logger.segnala("Richiesta per aggiungere un nuovo appello avviata \n");
            aggiornaPannello(mainPanel);
        });

        selettore.add(visualizzaCambiamentiButton);
        selettore.add(nonVisualizzaCambiamentiButton);
        selettore.add(aggiungiAppello);

        mainPanel.add(selettore,BorderLayout.NORTH);
        mainPanel.add(jScrollPane,BorderLayout.CENTER);
        mainPanel.add(panelAdderAppello,BorderLayout.SOUTH);


        f.add(mainPanel, BorderLayout.CENTER);

        f.pack();
        f.setVisible(true);

        return logger;
    }

    private static void aggiornaPannello(JPanel panel) {
        panel.revalidate();
        panel.repaint();
    }


}
