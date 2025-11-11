package view.panels;

import model.story.Choice;

import javax.swing.*;
import java.awt.*;

public class ChoicePanel extends JPanel {

    public interface Listener { void onChoiceSelected(String id); }

    private final JButton btnA = new JButton("A");
    private final JButton btnB = new JButton("B");

    public ChoicePanel(Listener listener) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 16, 8));
        add(btnA); add(btnB);
        btnA.setEnabled(false); btnB.setEnabled(false);
        btnA.addActionListener(e -> listener.onChoiceSelected("A"));
        btnB.addActionListener(e -> listener.onChoiceSelected("B"));
    }

    public void setChoices(Choice a, Choice b) {
        btnA.setText("A) " + a.getText());
        btnB.setText("B) " + b.getText());
        btnA.setEnabled(true);
        btnB.setEnabled(true);
    }
}
