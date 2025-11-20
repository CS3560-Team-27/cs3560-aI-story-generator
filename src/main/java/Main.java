import javax.swing.SwingUtilities;
import view.MainFrame;
import controller.MainController;

/**
 * Main entry point for the AI Story Generator application
 * Creates and launches the Swing GUI with the game controller
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create main window and controller
            MainFrame frame = new MainFrame();
            MainController controller = new MainController(frame);
            
            // Connect controller to UI and display
            frame.setController(controller);
            frame.setVisible(true);
        });
    }
}
