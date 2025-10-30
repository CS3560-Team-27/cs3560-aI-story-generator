package model.story;

import java.util.ArrayList;
import java.util.List;

public class StoryModel {
    private Character character;
    private World world;
    private StoryState state;
    private List<Scene> scenes;

    public StoryModel() {
        this.state = new StoryState();
        this.scenes = new ArrayList<>();
    }

    // Character
    public void setCharacter(Character character) {
        this.character = character;
    }

    public Character getCharacter() {
        return character;
    }

    // World
    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    // Scenes
    public void addScene(Scene scene) {
        scenes.add(scene);
        state.nextChapter(); // Each scene progression increases chapter count
    }

    public Scene getCurrentScene() {
        if (scenes.isEmpty()) {
            return null;
        }
        return scenes.get(scenes.size() - 1);
    }

    public List<Scene> getAllScenes() {
        return scenes;
    }

    // Story State
    public StoryState getState() {
        return state;
    }

    // Reset story for new run
    public void reset() {
        this.state = new StoryState();
        this.scenes.clear();
    }
}
