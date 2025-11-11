package view.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LibraryPanel extends JPanel {

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"Title", "Genre", "Chapters", "★ Favorite"}, 0);

    public LibraryPanel() {
        setLayout(new BorderLayout(8,8));
        add(new JLabel("Your Library"), BorderLayout.NORTH);
        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(new JButton("Open"));
        buttons.add(new JButton("Export .txt"));
        buttons.add(new JButton("Export .md"));
        add(buttons, BorderLayout.SOUTH);
    }

    // call later when LibraryModel is wired
    public void addRow(String title, String genre, int chapters, boolean fav) {
        model.addRow(new Object[]{title, genre, chapters, fav ? "★" : ""});
    }
}
