package servergui;

import javax.swing.*;
import java.awt.*;

public class Interface {
    public static void main(String[] args){
        JFrame f = new JFrame("Applicazione server");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setMinimumSize(new Dimension(1000, 400));
        f.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JTextArea logger = new JTextArea();
        logger.setEditable(false);
        logger.setSize(800,300);
        logger.setText("hosfvjifvhujnfvhjnfvjnbf" +
                "ohsdfvojsdfvhjnfvsjnosdfvjosfvjnsfvjn" +
                "vsjnosdfvjsdfvjvjnsfd \n"+"cvwerjvjernvjernvj \n"+"cvwerjvjernvjernvj \n");

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


        AddPanelFunction panelAdderAppello = new AddPanelFunction();
        panelAdderAppello.setVisible(false);
        JButton aggiungiAppello = new JButton("Aggiungi appello");
        aggiungiAppello.addActionListener( e -> {
            panelAdderAppello.setVisible(true);
            aggiungiAppello.setVisible(false);
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


    }

    private static void aggiornaPannello(JPanel panel) {
        panel.revalidate();
        panel.repaint();
    }


}
