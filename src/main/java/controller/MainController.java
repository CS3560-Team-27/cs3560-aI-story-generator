package controller;

import model.story.Character;
import model.story.World;
import model.story.Scene;
import model.story.Choice;
import model.story.StoryModel;
import model.story.SavedStory;
import service.OpenAIService;
import service.PromptBuilder;
import service.StoryLibrary;
import view.MainFrame;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

//Main controller that connects UI, story model, and AI service

public class MainController {

    private final MainFrame view;
    private final StoryModel model = new StoryModel();
    private final OpenAIService ai = new OpenAIService();

    // Story configuration set by user inputs
    private String genre = "fantasy";
    private String length = "Short";
    private String complexity = "Adult";
    private String style = "Descriptive";

    public MainController(MainFrame view) {
        this.view = view;
    }

    // ==================== User Input Handlers ====================

    public void onGenreSelected(String genreKey) {
        this.genre = genreKey;
    }

    public void onCharacterEntered(String name, List<String> traits, String backstory) {
        if (traits == null) traits = new ArrayList<>();
        Character c = new Character(name);
        c.setTraits(traits);
        c.setBackstory(backstory);
        model.setCharacter(c);
    }

    public void onWorldEntered(String location, String rule, String history) {
        World w = new World(location);
        w.setRule(rule);
        w.setHistory(history);
        model.setWorld(w);
    }

    public void onControlsSelected(String length, String complexity, String style) {
        this.length = length;
        this.complexity = complexity;
        this.style = style;
    }

    // ==================== Story Generation ====================

    public void startGame() {
        view.showLoading("Generating opening scene…");
        executeSceneGeneration(() -> {
            String prompt = PromptBuilder.introWithChoices(
                    model.getCharacter(), model.getWorld(),
                    genre, length, complexity, style
            );
            Scene s = ai.generateSceneWithChoices(prompt);
            model.addScene(s);
            return s;
        }, scene -> {
            view.showView(MainFrame.STORY);
            view.showStory(scene.getSceneText());
            view.setChoices(scene.getChoiceA(), scene.getChoiceB());
        }, "Failed to start story");
    }

    public void applyChoice(String id) {
        view.showLoading("Continuing story…");
        executeSceneGeneration(() -> {
            Scene current = model.getCurrentScene();
            if (current == null) throw new IllegalStateException("No current scene.");

            Choice chosen = "A".equalsIgnoreCase(id) ? current.getChoiceA() : current.getChoiceB();
            model.getState().nextChapter();

            String prompt = PromptBuilder.nextWithChoices(
                    current, chosen, genre, length, complexity, style, model.getState()
            );
            Scene next = ai.generateSceneWithChoices(prompt);
            model.addScene(next);
            return next;
        }, scene -> {
            view.showStory(scene.getSceneText());
            view.setChoices(scene.getChoiceA(), scene.getChoiceB());
        }, "Failed to continue story");
    }

    private void executeSceneGeneration(SceneSupplier supplier, SceneConsumer consumer, String errorMessage) {
        new SwingWorker<Scene, Void>() {
            @Override
            protected Scene doInBackground() throws Exception {
                return supplier.get();
            }

            @Override
            protected void done() {
                view.hideLoading();
                try {
                    consumer.accept(get());
                } catch (Exception ex) {
                    view.showError(errorMessage, unwrap(ex));
                }
            }
        }.execute();
    }

    @FunctionalInterface
    private interface SceneSupplier {
        Scene get() throws Exception;
    }

    @FunctionalInterface
    private interface SceneConsumer {
        void accept(Scene scene);
    }

    // ==================== Navigation ====================

    public void openLibrary() {
        view.showView(MainFrame.LIBRARY);
    }

    public void saveCurrentStory() {
        // Check if there's a story to save
        if (model.getAllScenes().isEmpty()) {
            view.showError("No Story to Save", new Exception("Please generate a story first before saving."));
            return;
        }
        
        // Ask user for a title
        String title = JOptionPane.showInputDialog(view, 
            "Enter a title for your story:", 
            "Save Story", 
            JOptionPane.QUESTION_MESSAGE);
        
        if (title == null) {
            // User cancelled
            return;
        }
        
        // Save in background thread
        new SwingWorker<SavedStory, Void>() {
            @Override
            protected SavedStory doInBackground() throws Exception {
                return StoryLibrary.getInstance().saveStory(
                    model, genre, title, length, complexity, style
                );
            }
            
            @Override
            protected void done() {
                try {
                    SavedStory saved = get();
                    JOptionPane.showMessageDialog(view, 
                        "Story saved successfully!\n\n" +
                        "Title: " + saved.getDisplayTitle() + "\n" +
                        "Chapters: " + saved.getTotalChapters() + "\n" +
                        "Saved: " + saved.getFormattedCreatedDate(),
                        "Story Saved", 
                        JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    view.showError("Failed to save story", unwrap(ex));
                }
            }
        }.execute();
    }

    // ==================== Utility ====================

    // Unwraps nested RuntimeExceptions to find the root cause
    private static Exception unwrap(Exception ex) {
        Throwable t = ex;
        while (t instanceof RuntimeException && t.getCause() != null) {
            t = t.getCause();
        }
        return (t instanceof Exception) ? (Exception) t : new Exception(t);
    }
}