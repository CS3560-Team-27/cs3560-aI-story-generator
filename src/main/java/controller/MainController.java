package controller;

import model.image.ImageRequestType;
import model.strategy.AdultMode;
import model.strategy.ChildFriendlyMode;
import model.strategy.StoryModeStrategy;
import model.story.*;
import service.ImageGenerationService;
import service.OpenAIService;
import service.PromptBuilder;
import service.StorySaveSystem;
import view.MainFrame;
import view.panels.ImagePanel;

import javax.swing.*;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * MainController
 *
 * Core application logic:
 *   • Handles UI events and flow between screens
 *   • Manages StoryModel lifecycle
 *   • Generates new scenes using OpenAI (async)
 *   • Generates scene illustrations (async, optional)
 *   • Saves / loads progress via StorySaveSystem
 *
 * DESIGN PATTERNS USED:
 *   - MVC Controller
 *   - Strategy Pattern (AdultMode vs ChildFriendlyMode)
 *   - Factory Pattern (ImagePromptFactory creation)
 *   - Observer-style updates to view
 */
public class MainController {

    /* ---------------------------------------------------------
       Fields
       --------------------------------------------------------- */

    private final MainFrame mainFrame;
    private final StorySaveSystem saveSystem = new StorySaveSystem();

    /** Replaced entirely if a saved game is loaded */
    private StoryModel storyModel = new StoryModel();

    private final PromptBuilder promptBuilder = new PromptBuilder();
    private final OpenAIService api = new OpenAIService();
    private final ImageGenerationService imageService = new ImageGenerationService();

    private String selectedGenre;
    private String selectedLength;
    private String selectedComplexity;
    private String selectedStyle;

    /** STRATEGY PATTERN: runtime-selected story mode */
    private StoryModeStrategy modeStrategy;

    /* ---------------------------------------------------------
       Constructor
       --------------------------------------------------------- */
    public MainController(MainFrame frame) {
        this.mainFrame = frame;
        frame.setController(this);
    }

    /* =========================================================
       GENRE / CHARACTER / WORLD / CONTROLS SETUP
       ========================================================= */

    public void onGenreSelected(String g) {
        this.selectedGenre = g;
        storyModel.setGenre(g);
    }

    public void onCharacterEntered(String name, List<String> traits, String backstory) {
        storyModel.setCharacter(new CharacterModel(name, traits, backstory));
    }

    public void onWorldEntered(String location, String rule, String history) {
        storyModel.setWorld(new WorldModel(location, rule, history));
    }

    public void onControlsSelected(String length, String complexity, String style) {
        this.selectedLength = length;
        this.selectedComplexity = complexity;
        this.selectedStyle = style;

        // STRATEGY PATTERN selection
        if ("Child-Friendly".equalsIgnoreCase(complexity)) {
            modeStrategy = new ChildFriendlyMode();
        } else {
            modeStrategy = new AdultMode();
        }

        promptBuilder.setModeStrategy(modeStrategy);
    }

    /* =========================================================
       START NEW STORY
       ========================================================= */

    public void startGame() {
        storyModel.reset();
        requestNextScene(null);
        mainFrame.showView(MainFrame.STORY);
    }

    /* =========================================================
       APPLY PLAYER CHOICE
       ========================================================= */

    public void applyChoice(String id) {

        SceneModel current = storyModel.getCurrentScene();
        if (current == null || current.isEnding() || storyModel.isComplete())
            return;

        ChoiceModel chosen = switch (id) {
            case "A" -> current.getChoiceA();
            case "B" -> current.getChoiceB();
            case "C" -> current.getChoiceC();
            default -> null;
        };

        if (chosen == null) return;

        int chapter = storyModel.getState().getChapter();
        storyModel.getState().addChoiceRecord(
                new ChoiceRecordModel(chapter, id, chosen.getText())
        );

        storyModel.nextChapter();
        requestNextScene(id);
    }

    /* =========================================================
       GENERATE NEXT SCENE (ASYNC)
       ========================================================= */

    private void requestNextScene(String lastChoiceId) {

        int chapter = storyModel.getState().getChapter();
        mainFrame.showLoading("Generating chapter " + chapter + "...");

        new SwingWorker<SceneModel, Void>() {

            @Override
            protected SceneModel doInBackground() throws Exception {
                String prompt = promptBuilder.buildStoryPrompt(
                        storyModel,
                        lastChoiceId,
                        selectedLength,
                        selectedComplexity,
                        selectedStyle
                );
                return api.generateScene(prompt);
            }

            @Override
            protected void done() {
                try {
                    SceneModel scene = get();
                    storyModel.setCurrentScene(scene);

                    mainFrame.showScene(scene);

                    boolean enable = !(scene.isEnding() || storyModel.isComplete());
                    mainFrame.getChoicePanel().setButtonsEnabled(enable);

                    // Auto-generate image (safe placeholder)
                    autoGenerateSceneImage();

                } catch (Exception ex) {
                    showFriendlyAIError(ex);
                } finally {
                    mainFrame.hideLoading();
                }
            }
        }.execute();
    }

    /* =========================================================
       AUTO IMAGE GENERATION (CACHEING)
       ========================================================= */

    private void autoGenerateSceneImage() {

        if (storyModel.getCurrentScene() == null) return;

        ImagePanel imgPanel = mainFrame.getStoryPanel().getImagePanel();
        int chapter = storyModel.getState().getChapter();

        // ------------------------------------------------------
        // 1) CHECK CACHE FIRST (NO API CALL)
        // ------------------------------------------------------
        Image cached = storyModel.getCachedImage(chapter);
        if (cached != null) {
            imgPanel.setImage(cached);
            return;
        }

        // ------------------------------------------------------
        // 2) NOT CACHED → GENERATE (ASYNC)
        // ------------------------------------------------------
        imgPanel.showLoadingIcon();

        new SwingWorker<Image, Void>() {

            @Override
            protected Image doInBackground() {

                String prompt = imageService.buildImagePromptForStory(
                        storyModel,
                        selectedStyle,
                        ImageRequestType.SCENE_MOMENT
                );

                System.out.println("Image prompt:\n" + prompt);

                // Real image OR placeholder fallback
                return imageService.generateImageWithFallback(prompt);
            }

            @Override
            protected void done() {
                try {
                    Image img = get();
                    if (img != null) {
                        // --------------------------------------------------
                        // 3) CACHE IMAGE FOR THIS CHAPTER
                        // --------------------------------------------------
                        storyModel.cacheImage(chapter, img);
                        imgPanel.setImage(img);
                    } else {
                        imgPanel.clearImage();
                    }
                } catch (Exception ex) {
                    imgPanel.clearImage();
                }
            }
        }.execute();
    }

    /* =========================================================
       FRIENDLY ERROR HANDLING (RATE LIMIT SAFE)
       ========================================================= */

    private void showFriendlyAIError(Exception ex) {

        String message = """
            The story could not be generated right now.

            This usually happens when:
            • Too many AI requests were made recently
            • Your account hit a temporary rate limit
            • The AI service is under heavy load

            Please wait a moment and try again.
            """;

        JOptionPane.showMessageDialog(
                mainFrame,
                message,
                "AI Service Busy",
                JOptionPane.WARNING_MESSAGE
        );
    }

    /* =========================================================
       LIBRARY
       ========================================================= */

    public void openLibrary() {
        mainFrame.showView(MainFrame.LIBRARY);
    }

    /* =========================================================
       SAVE STORY
       ========================================================= */

    public void saveCurrentStory() {
        try {
            SavedStoryModel saved = new SavedStoryModel(
                    null,
                    storyModel.getGenre(),
                    storyModel.getCharacter(),
                    storyModel.getWorld(),
                    storyModel.getAllScenes(),
                    "{}"
            );

            saved.setChoiceHistory(
                    new ArrayList<>(storyModel.getState().getChoiceHistory())
            );

            File saveFile = saveSystem.saveGame(saved);

            JOptionPane.showMessageDialog(
                    mainFrame,
                    "Saved:\n" + saveFile.getName()
            );

        } catch (Exception ex) {
            mainFrame.showError("Save Error", ex);
        }
    }

    /* =========================================================
       LOAD STORY
       ========================================================= */

    public void loadSaveFile(File file) {
        try {
            SavedStoryModel saved = saveSystem.loadGame(file);
            if (saved == null) return;

            storyModel = new StoryModel();
            storyModel.setGenre(saved.getGenre());
            storyModel.setCharacter(saved.getCharacter());
            storyModel.setWorld(saved.getWorld());
            storyModel.setScenes(new ArrayList<>(saved.getScenes()));

            if (saved.getChoiceHistory() != null)
                storyModel.setChoiceHistory(new ArrayList<>(saved.getChoiceHistory()));

            int chapterCount = saved.getScenes().size();
            storyModel.setCurrentChapter(chapterCount);
            storyModel.restoreCurrentSceneAfterLoad();

            mainFrame.showScene(storyModel.getCurrentScene());
            mainFrame.showView(MainFrame.STORY);

            autoGenerateSceneImage();

        } catch (Exception ex) {
            mainFrame.showError("Load Error", ex);
        }
    }

    /* =========================================================
       TEST HELPERS
       ========================================================= */

    public StoryModel getStoryModel() { return storyModel; }
    public String getSelectedLength() { return selectedLength; }
    public String getSelectedComplexity() { return selectedComplexity; }
    public String getSelectedStyle() { return selectedStyle; }
}
