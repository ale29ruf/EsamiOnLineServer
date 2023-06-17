package servergui;

import javax.swing.*;
import java.awt.*;

public class Interface {
    public static void main(String[] args){
        JFrame f = new JFrame("Applicazione server");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setMinimumSize(new Dimension(1000, 400));
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        JPanel panel = new JPanel();
        panel.setSize(new Dimension(700,100));

        JTextArea logger = new JTextArea();
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
        });

        JButton nonVisualizzaCambiamentiButton = new JButton("Disabilita visualizzazione cambiamenti");
        nonVisualizzaCambiamentiButton.addActionListener( e -> {
            logger.setVisible(false);
            jScrollPane.setVisible(false);
        });

        AddPanelFunction panelAdderAppello = new AddPanelFunction();
        panelAdderAppello.setVisible(false);
        JButton aggiungiAppello = new JButton("Aggiungi appello");
        aggiungiAppello.addActionListener( e -> {
            panelAdderAppello.setVisible(true);
        });

        selettore.add(visualizzaCambiamentiButton);
        selettore.add(nonVisualizzaCambiamentiButton);
        selettore.add(aggiungiAppello);

        panel.add(selettore,BorderLayout.NORTH);
        panel.add(jScrollPane,BorderLayout.CENTER);
        panel.add(panelAdderAppello,BorderLayout.SOUTH);


        f.add(panel, BorderLayout.CENTER);

        f.pack();

    }


}
