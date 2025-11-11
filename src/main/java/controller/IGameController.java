package controller;

import java.util.List;

public interface IGameController {
    void onGenreSelected(String genre);
    void onCharacterEntered(String name, List<String> traits, String backstory);
    void onWorldEntered(String location, String rule, String history);
    void onControlsSelected(String length, String complexity, String style);
    void startGame();              // generate intro
    void applyChoice(String id);   // "A" or "B"
    void openLibrary();
    void saveCurrentStory();
}
