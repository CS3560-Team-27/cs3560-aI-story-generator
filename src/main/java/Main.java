import javax.swing.SwingUtilities;
import view.MainFrame;
import controller.DemoController;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            // Wire up a temporary demo controller so the app runs end-to-end today
            DemoController controller = new DemoController(frame);
            frame.setController(controller);
            frame.setVisible(true);
        });
    }
}
