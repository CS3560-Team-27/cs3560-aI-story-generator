package model.story;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StoryModelTest {

    @Test
    void testCharacterStorage() {
        StoryModel model = new StoryModel();
        Character c = new Character("Elowen");

        model.setCharacter(c);

        assertEquals(c, model.getCharacter());
    }

    @Test
    void testWorldStorage() {
        StoryModel model = new StoryModel();
        World w = new World("Forest");

        model.setWorld(w);

        assertEquals(w, model.getWorld());
    }

    @Test
    void testAddScene() {
        StoryModel model = new StoryModel();
        Scene s = new Scene("Intro", new Choice("A", "Go"), new Choice("B", "Stop"));

        model.addScene(s);

        assertEquals(1, model.getAllScenes().size());
        assertEquals(s, model.getCurrentScene());
    }

    @Test
    void testReset() {
        StoryModel model = new StoryModel();
        model.addScene(new Scene("Test", null, null));

        model.reset();

        assertTrue(model.getAllScenes().isEmpty());
        assertEquals(1, model.getState().getChapter());
    }
}
