package view;

import controller.MainController;
import model.story.Choice;
import view.components.ErrorDialog;
import view.components.LoadingIndicator;
import view.panels.GenrePanel;
import view.panels.CharacterPanel;
import view.panels.WorldPanel;
import view.panels.ControlsPanel;
import view.panels.ChoicePanel;
import view.panels.StoryPanel;
import view.panels.LibraryPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame
        implements GenrePanel.Listener, CharacterPanel.Listener, WorldPanel.Listener,
        ControlsPanel.Listener, ChoicePanel.Listener {

    public static final String GENRE = "GENRE";
    public static final String CHARACTER = "CHARACTER";
    public static final String WORLD = "WORLD";
    public static final String CONTROLS = "CONTROLS";
    public static final String STORY = "STORY";
    public static final String LIBRARY = "LIBRARY";

    private final CardLayout cards = new CardLayout();
    private final JPanel cardHolder = new JPanel(cards);
    private final StoryPanel storyPanel = new StoryPanel();
    private final LoadingIndicator loading = new LoadingIndicator();
    private MainController controller;

    public MainFrame() {
        super("AI Story Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 700));
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        add(buildToolbar(), BorderLayout.NORTH);
        add(cardHolder, BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);

        GenrePanel genrePanel = new GenrePanel(this);
        cardHolder.add(genrePanel, GENRE);
        CharacterPanel characterPanel = new CharacterPanel(this);
        cardHolder.add(characterPanel, CHARACTER);
        WorldPanel worldPanel = new WorldPanel(this);
        cardHolder.add(worldPanel, WORLD);
        ControlsPanel controlsPanel = new ControlsPanel(this);
        cardHolder.add(controlsPanel, CONTROLS);
        cardHolder.add(storyPanel, STORY);
        LibraryPanel libraryPanel = new LibraryPanel();
        cardHolder.add(libraryPanel, LIBRARY);

        setGlassPane(loading);
        showView(GENRE);
        pack();
    }

    public void setController(MainController controller) {
        this.controller = controller;
    }

    private JToolBar buildToolbar() {
        JToolBar tb = new JToolBar();
        tb.setFloatable(false);

        JButton back = new JButton("Back");
        JButton library = new JButton("Library");
        JButton save = new JButton("Save");
        JButton help = new JButton("Help");

        back.addActionListener(e -> cards.previous(cardHolder));
        library.addActionListener(e -> {
            if (controller != null) controller.openLibrary();
        });
        save.addActionListener(e -> {
            if (controller != null) controller.saveCurrentStory();
        });
        help.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Choose a genre, create a character and world, then begin the story."));

        tb.add(back);
        tb.add(library);
        tb.add(save);
        tb.add(Box.createHorizontalGlue());
        tb.add(help);
        return tb;
    }

    private JPanel buildStatusBar() {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel("Ready"), BorderLayout.WEST);
        return p;
    }

    public void showView(String key) {
        cards.show(cardHolder, key);
    }

    public void showLoading(String msg) {
        loading.show(msg);
    }

    public void hideLoading() {
        loading.hideIt();
    }

    public void showError(String title, Exception ex) {
        ErrorDialog.show(this, title, ex);
    }

    public void showStory(String text) {
        storyPanel.setStoryText(text);
    }

    public void setChoices(Choice a, Choice b) {
        storyPanel.setChoices(a, b, this);
    }

    @Override
    public void onGenreChosen(String genreKey) {
        if (controller != null) controller.onGenreSelected(genreKey);
        showView(CHARACTER);
    }

    @Override
    public void onCharacterContinue(String name, List<String> traits, String backstory) {
        if (controller != null) controller.onCharacterEntered(name, traits, backstory);
        showView(WORLD);
    }

    @Override
    public void onWorldContinue(String location, String rule, String history) {
        if (controller != null) controller.onWorldEntered(location, rule, history);
        showView(CONTROLS);
    }

    @Override
    public void onControlsBegin(String length, String complexity, String style) {
        if (controller != null) {
            controller.onControlsSelected(length, complexity, style);
            controller.startGame();
        }
    }

    @Override
    public void onChoiceSelected(String id) {
        if (controller != null) controller.applyChoice(id);
    }
}
