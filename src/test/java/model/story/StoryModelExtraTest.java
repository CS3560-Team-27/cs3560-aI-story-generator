package model.story;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class StoryModelExtraTest {

    @Test
    void testGetCurrentSceneWhenEmpty() {
        StoryModel model = new StoryModel();
        assertNull(model.getCurrentScene());
    }

    @Test
    void testAddMultipleScenes() {
        StoryModel model = new StoryModel();

        IntStream.range(0, 5).forEach(i -> {
            model.addScene(new Scene("Scene " + i, null, null));
        });

        assertEquals(5, model.getAllScenes().size());
        assertEquals("Scene 4", model.getCurrentScene().getSceneText());
    }
}
