package model.image;

import model.story.CharacterModel;
import model.story.SceneModel;
import model.story.WorldModel;

/**
 * ImagePromptContext
 *
 * A data bundle passed into any ImagePromptFactory implementation.
 * It contains:
 *   - genre (Fantasy, Sci-Fi, etc.)
 *   - selected style (Descriptive, Neutral, Terse)
 *   - current chapter
 *   - character info
 *   - world info
 *   - current scene
 *
 * FACTORY PATTERN:
 *   Instead of factories pulling directly from StoryModel,
 *   we pass only the necessary fields bundled together.
 */
public class ImagePromptContext {

    private final String genre;
    private final String style;
    private final int chapter;

    private final CharacterModel character;
    private final WorldModel world;
    private final SceneModel scene;

    public ImagePromptContext(String genre,
                              String style,
                              int chapter,
                              CharacterModel character,
                              WorldModel world,
                              SceneModel scene) {

        this.genre = genre;
        this.style = style;
        this.chapter = chapter;
        this.character = character;
        this.world = world;
        this.scene = scene;
    }

    public String getGenre() { return genre; }
    public String getStyle() { return style; }
    public int getChapter() { return chapter; }

    public CharacterModel getCharacter() { return character; }
    public WorldModel getWorld() { return world; }
    public SceneModel getScene() { return scene; }
}
