package view.panels;

import javax.swing.*;
import java.awt.*;

/**
 * ImagePanel
 *
 * Displays the scene image or a fallback placeholder.
 * Contains helper methods for loading, error, and normal states.
 */
public class ImagePanel extends JPanel {

    private Image image = null;
    private State state = State.EMPTY;

    private enum State {
        EMPTY,
        LOADING,
        ERROR,
        IMAGE
    }

    public ImagePanel() {
        setPreferredSize(new Dimension(300, 400));
        setBackground(new Color(235, 235, 235));
    }

    /* ============================================================
       PUBLIC METHODS CALLED BY CONTROLLER / STORY PANEL
       ============================================================ */

    public void setImage(Image img) {
        this.image = img;
        this.state = State.IMAGE;
        repaint();
    }

    public void clearImage() {
        this.image = null;
        this.state = State.EMPTY;
        repaint();
    }

    public void showLoadingIcon() {
        this.image = null;
        this.state = State.LOADING;
        repaint();
    }

    public void showErrorIcon() {
        this.image = null;
        this.state = State.ERROR;
        repaint();
    }

    /* ============================================================
       PAINTING LOGIC
       ============================================================ */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        switch (state) {

            case LOADING -> drawCenteredText(g, "Generating image...");
            case ERROR -> drawCenteredText(g, "Failed to load image");
            case EMPTY -> drawCenteredText(g, "No image");
            case IMAGE -> drawScaledImage(g);
        }
    }

    private void drawCenteredText(Graphics g, String text) {
        g.setColor(new Color(180, 180, 180));
        g.setFont(new Font("Georgia", Font.PLAIN, 14));

        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int x = (getWidth() - textWidth) / 2;
        int y = getHeight() / 2;

        g.drawString(text, x, y);
    }

    private void drawScaledImage(Graphics g) {
        if (image == null) return;

        int panelW = getWidth();
        int panelH = getHeight();
        int imgW = image.getWidth(null);
        int imgH = image.getHeight(null);

        if (imgW <= 0 || imgH <= 0) return;

        double scale = Math.min((double) panelW / imgW, (double) panelH / imgH);
        int newW = (int) (imgW * scale);
        int newH = (int) (imgH * scale);

        int x = (panelW - newW) / 2;
        int y = (panelH - newH) / 2;

        g.drawImage(image, x, y, newW, newH, null);
    }
}
