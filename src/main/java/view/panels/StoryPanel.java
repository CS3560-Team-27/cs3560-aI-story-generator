package view.panels;

import model.story.Choice;

import javax.swing.*;
import java.awt.*;

public class StoryPanel extends JPanel {

    private final JTextArea storyArea = new JTextArea(16, 80);
    private ChoicePanel choices;

    public StoryPanel() {
        setLayout(new BorderLayout(8,8));
        storyArea.setEditable(false);
        storyArea.setLineWrap(true);
        storyArea.setWrapStyleWord(true);

        choices = new ChoicePanel(id -> {}); // placeholder; will be set via setChoices(...)
        add(new JScrollPane(storyArea), BorderLayout.CENTER);
        add(choices, BorderLayout.SOUTH);
    }

    public void setStoryText(String text) {
        storyArea.setText(text);
        storyArea.setCaretPosition(0);
    }

    public void setChoices(Choice a, Choice b, ChoicePanel.Listener listener) {
        remove(choices);
        choices = new ChoicePanel(listener);
        choices.setChoices(a, b);
        add(choices, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }
}
