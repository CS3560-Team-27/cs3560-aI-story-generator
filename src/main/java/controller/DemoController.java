package controller;

import model.story.Scene;
import model.story.World;
import model.story.Choice;
import model.story.StoryModel;
import model.strategy.AIStrategy;
import model.strategy.StrategyFactory;
import view.MainFrame;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Temporary controller so the UI is fully runnable today.
 * Uses your StrategyFactory + placeholder text to generate scenes.
 */
public class DemoController implements IGameController {

    private final MainFrame view;

    // Session state (re-used model objects)
    private final StoryModel storyModel = new StoryModel();
    private AIStrategy strategy;
    private String selectedGenre = "fantasy";

    public DemoController(MainFrame view) {
        this.view = view;
    }

    @Override
    public void onGenreSelected(String genre) {
        this.selectedGenre = genre;
    }

    @Override
    public void onCharacterEntered(String name, List<String> traits, String backstory) {
        if (traits == null) traits = new ArrayList<>();
        storyModel.setCharacter(new model.story.Character(name, traits, backstory));
    }

    @Override
    public void onWorldEntered(String location, String rule, String history) {
        storyModel.setWorld(new World(location, rule, history));
    }

    @Override
    public void onControlsSelected(String length, String complexity, String style) {
    }

    @Override
    public void startGame() {
        view.showLoading("Summoning the intro…");
        new SwingWorker<Scene, Void>() {
            @Override protected Scene doInBackground() {
                try {
                    strategy = StrategyFactory.create(selectedGenre);
                    model.story.Character c = storyModel.getCharacter(); // explicit type
                    World w = storyModel.getWorld();
                    Thread.sleep(600); // simulate latency
                    return strategy.generateIntro(c, w);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            @Override protected void done() {
                view.hideLoading();
                try {
                    Scene s = get();
                    storyModel.addScene(s);
                    view.showStory(s.getSceneText());
                    view.setChoices(s.getChoiceA(), s.getChoiceB());
                    view.showView(MainFrame.STORY);
                } catch (Exception ex) {
                    view.showError("Failed to start story", ex);
                }
            }
        }.execute();
    }

    @Override
    public void applyChoice(String id) {
        view.showLoading("Writing the next scene…");
        new SwingWorker<Scene, Void>() {
            @Override protected Scene doInBackground() {
                try {
                    Scene current = storyModel.getCurrentScene();
                    Choice chosen = "A".equals(id) ? current.getChoiceA() : current.getChoiceB();
                    Thread.sleep(600); // simulate latency
                    return strategy.generateNext(chosen, storyModel.getState());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            @Override protected void done() {
                view.hideLoading();
                try {
                    Scene next = get();
                    storyModel.addScene(next);
                    view.showStory(next.getSceneText());
                    view.setChoices(next.getChoiceA(), next.getChoiceB());
                } catch (Exception ex) {
                    view.showError("Failed to continue story", ex);
                }
            }
        }.execute();
    }

    @Override public void openLibrary() { view.showView(MainFrame.LIBRARY); }

    @Override
    public void saveCurrentStory() {
        JOptionPane.showMessageDialog(view, "Saved to Library (demo)");
    }
}
