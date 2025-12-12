package view.panels;

import model.story.SceneModel;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

/**
 * StoryPanel (Upgraded + Image Panel Version)
 *
 * Features:
 *  • Left-side ImagePanel for automatic AI illustrations
 *  • Right-side rich text story area (visual novel style)
 *  • StyledDocument typography with spacing + indentation
 *  • Soft fade-in animation for new scenes
 *  • Scrollable reading area
 *  • Loading indicator
 */
public class StoryPanel extends JPanel {

    private final ImagePanel imagePanel = new ImagePanel();  // NEW
    private final JTextPane textPane = new JTextPane();
    private final JScrollPane scrollPane;
    private final JLabel loadingLabel;

    public StoryPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        /* ============================================================
           MAIN SPLIT LAYOUT: LEFT (IMAGE) | RIGHT (STORY)
           ============================================================ */
        JPanel splitPanel = new JPanel(new GridBagLayout());
        add(splitPanel, BorderLayout.CENTER);

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(10, 10, 10, 10);
        gc.fill = GridBagConstraints.BOTH;

        /* ------------------------------------------------------------
           LEFT PANEL: IMAGE
           ------------------------------------------------------------ */
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 0.35;    // 35% width
        gc.weighty = 1.0;
        imagePanel.setPreferredSize(new Dimension(350, 500));
        splitPanel.add(imagePanel, gc);

        /* ------------------------------------------------------------
           RIGHT PANEL: STORY TEXT AREA
           ------------------------------------------------------------ */
        gc.gridx = 1;
        gc.weightx = 0.65;    // 65% width

        JPanel storyAreaPanel = new JPanel(new BorderLayout());
        storyAreaPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        textPane.setEditable(false);
        textPane.setFont(new Font("georgia", Font.PLAIN, 16));
        textPane.setMargin(new Insets(20, 40, 20, 40)); // padding inside text
        textPane.setBackground(new Color(250, 248, 245));

        scrollPane = new JScrollPane(textPane);
        scrollPane.setBorder(null);

        storyAreaPanel.add(scrollPane, BorderLayout.CENTER);
        splitPanel.add(storyAreaPanel, gc);

        /* ============================================================
           LOADING LABEL (BOTTOM OF RIGHT PANEL)
           ============================================================ */
        loadingLabel = new JLabel("Loading...", SwingConstants.CENTER);
        loadingLabel.setFont(new Font("georgia", Font.BOLD, 18));
        loadingLabel.setVisible(false);

        storyAreaPanel.add(loadingLabel, BorderLayout.SOUTH);
    }

    /* ============================================================
       PUBLIC API – Called by MainController
       ============================================================ */

    /** Updates story text and triggers fade-in animation */
    public void showScene(SceneModel scene) {
        String formatted = formatParagraphs(scene.getStoryText());
        renderText(formatted);
        startFadeInAnimation();
    }

    public void setStoryText(String text) {
        renderText(formatParagraphs(text));
    }

    public void showLoadingState(boolean loading) {
        loadingLabel.setVisible(loading);
    }

    /** Allows MainController to update the image */
    public ImagePanel getImagePanel() {
        return imagePanel;
    }

    /* ============================================================
       TEXT RENDERING
       ============================================================ */

    private void renderText(String text) {
        StyledDocument doc = textPane.getStyledDocument();
        doc.putProperty(DefaultEditorKit.EndOfLineStringProperty, "\n");

        try {
            doc.remove(0, doc.getLength());

            String[] paragraphs = text.split("\n\n");

            for (String p : paragraphs) {
                insertStyledParagraph(doc, p.trim());
                doc.insertString(doc.getLength(), "\n\n", null);
            }

        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        textPane.setCaretPosition(0);
    }

    private void insertStyledParagraph(StyledDocument doc, String text)
            throws BadLocationException {

        SimpleAttributeSet attrs = new SimpleAttributeSet();

        StyleConstants.setFirstLineIndent(attrs, 30f);
        StyleConstants.setLineSpacing(attrs, 0.2f);
        StyleConstants.setForeground(attrs, new Color(45, 38, 32));

        doc.insertString(doc.getLength(), text, attrs);
    }

    /* ============================================================
       AUTO-PARAGRAPHING
       ============================================================ */

    private String formatParagraphs(String text) {
        if (text == null) return "";
        text = text.trim().replace("\r", "");

        if (text.contains("\n\n"))
            return text;

        text = text.replaceAll("\\.\\s+(?=[A-Z])", ".\n\n");
        text = text.replaceAll("([!?])\\s+(?=[A-Z])", "$1\n\n");

        return text;
    }

    /* ============================================================
       FADE-IN ANIMATION
       ============================================================ */

    private void startFadeInAnimation() {
        textPane.setForeground(new Color(45, 38, 32, 0));

        Timer timer = new Timer(20, null);
        timer.addActionListener(e -> {
            Color c = textPane.getForeground();
            int alpha = Math.min(255, c.getAlpha() + 15);
            textPane.setForeground(new Color(45, 38, 32, alpha));

            if (alpha >= 255) timer.stop();
        });

        timer.start();
    }
}
