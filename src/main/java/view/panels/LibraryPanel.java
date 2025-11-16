package view.panels;

import model.story.SavedStory;
import service.StoryLibrary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * LibraryPanel - Shows saved stories and allows viewing/managing them
 */
public class LibraryPanel extends JPanel {
    
    private final DefaultListModel<SavedStory> listModel;
    private final JList<SavedStory> storyList;
    private final JTextArea previewArea;
    private final JLabel statusLabel;

    public LibraryPanel() {
        setLayout(new BorderLayout());
        
        // Create components
        listModel = new DefaultListModel<>();
        storyList = new JList<>(listModel);
        previewArea = new JTextArea();
        statusLabel = new JLabel("Loading library...");
        
        initializeComponents();
        layoutComponents();
        loadStoriesFromLibrary();
    }
    
    private void initializeComponents() {
        // Configure story list
        storyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        storyList.setCellRenderer(new StoryListRenderer());
        storyList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedStoryPreview();
            }
        });
        
        // Configure preview area
        previewArea.setEditable(false);
        previewArea.setLineWrap(true);
        previewArea.setWrapStyleWord(true);
        previewArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        previewArea.setText("Select a story from the list to preview it here.");
        
        // Configure status label
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }
    
    private void layoutComponents() {
        // Left panel - story list
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Saved Stories"));
        leftPanel.add(new JScrollPane(storyList), BorderLayout.CENTER);
        
        // Add refresh and delete buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton refreshButton = new JButton("Refresh");
        JButton deleteButton = new JButton("Delete");
        JButton exportButton = new JButton("Export");
        
        refreshButton.addActionListener(e -> loadStoriesFromLibrary());
        deleteButton.addActionListener(this::deleteSelectedStory);
        exportButton.addActionListener(this::exportSelectedStory);
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(exportButton);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Right panel - preview
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Story Preview"));
        rightPanel.add(new JScrollPane(previewArea), BorderLayout.CENTER);
        
        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(300);
        
        add(splitPane, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
    }
    
    private void loadStoriesFromLibrary() {
        SwingUtilities.invokeLater(() -> {
            try {
                statusLabel.setText("Loading stories...");
                List<SavedStory> stories = StoryLibrary.getInstance().getAllSavedStories();
                
                listModel.clear();
                for (SavedStory story : stories) {
                    listModel.addElement(story);
                }
                
                statusLabel.setText("Loaded " + stories.size() + " stories");
                
                if (stories.isEmpty()) {
                    previewArea.setText("No saved stories found.\n\nCreate and save a story to see it appear here!");
                }
                
            } catch (Exception e) {
                statusLabel.setText("Error loading stories: " + e.getMessage());
                previewArea.setText("Error loading stories: " + e.getMessage());
            }
        });
    }
    
    private void showSelectedStoryPreview() {
        SavedStory selected = storyList.getSelectedValue();
        if (selected == null) {
            previewArea.setText("Select a story from the list to preview it here.");
            return;
        }
        
        try {
            // Load full story details
            SavedStory fullStory = StoryLibrary.getInstance().loadStory(selected.getId());
            
            StringBuilder preview = new StringBuilder();
            preview.append("=== ").append(fullStory.getDisplayTitle()).append(" ===\n\n");
            preview.append("Genre: ").append(fullStory.getGenre()).append("\n");
            preview.append("Created: ").append(fullStory.getFormattedCreatedDate()).append("\n");
            preview.append("Chapters: ").append(fullStory.getTotalChapters()).append("\n\n");
            
            if (fullStory.getCharacter() != null) {
                preview.append("Character: ").append(fullStory.getCharacter().getName()).append("\n");
                if (fullStory.getCharacter().getBackstory() != null && !fullStory.getCharacter().getBackstory().isEmpty()) {
                    preview.append("Backstory: ").append(fullStory.getCharacter().getBackstory()).append("\n");
                }
            }
            
            if (fullStory.getWorld() != null) {
                preview.append("Setting: ").append(fullStory.getWorld().getLocation()).append("\n\n");
            }
            
            preview.append("=== STORY ===\n\n");
            
            if (fullStory.getScenes() != null && !fullStory.getScenes().isEmpty()) {
                for (int i = 0; i < Math.min(3, fullStory.getScenes().size()); i++) {
                    preview.append("Chapter ").append(i + 1).append(":\n");
                    preview.append(fullStory.getScenes().get(i).getSceneText()).append("\n\n");
                }
                
                if (fullStory.getScenes().size() > 3) {
                    preview.append("... and ").append(fullStory.getScenes().size() - 3).append(" more chapters");
                }
            }
            
            previewArea.setText(preview.toString());
            previewArea.setCaretPosition(0); // Scroll to top
            
        } catch (Exception e) {
            previewArea.setText("Error loading story details: " + e.getMessage());
        }
    }
    
    private void deleteSelectedStory(ActionEvent e) {
        SavedStory selected = storyList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a story to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete the story:\n\"" + selected.getDisplayTitle() + "\"?\n\nThis cannot be undone.", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            boolean deleted = StoryLibrary.getInstance().deleteStory(selected.getId());
            if (deleted) {
                loadStoriesFromLibrary(); // Refresh the list
                previewArea.setText("Story deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete the story.", "Delete Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void exportSelectedStory(ActionEvent e) {
        SavedStory selected = storyList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a story to export.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            SavedStory fullStory = StoryLibrary.getInstance().loadStory(selected.getId());
            
            StringBuilder export = new StringBuilder();
            export.append(fullStory.getDisplayTitle()).append("\n");
            export.append("=".repeat(fullStory.getDisplayTitle().length())).append("\n\n");
            
            if (fullStory.getScenes() != null) {
                for (int i = 0; i < fullStory.getScenes().size(); i++) {
                    export.append("Chapter ").append(i + 1).append("\n");
                    export.append("-".repeat(20)).append("\n");
                    export.append(fullStory.getScenes().get(i).getSceneText()).append("\n\n");
                }
            }
            
            // Show in a dialog for now (could be extended to save to file)
            JTextArea exportArea = new JTextArea(export.toString());
            exportArea.setEditable(false);
            exportArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(exportArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Exported Story: " + selected.getDisplayTitle(), JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to export story: " + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Custom renderer for the story list
     */
    private static class StoryListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (value instanceof SavedStory) {
                SavedStory story = (SavedStory) value;
                setText("<html><b>" + story.getDisplayTitle() + "</b><br>" +
                       "<small>" + story.getGenre() + " • " + story.getTotalChapters() + " chapters • " + 
                       story.getFormattedCreatedDate() + "</small></html>");
            }
            
            return this;
        }
    }
}
