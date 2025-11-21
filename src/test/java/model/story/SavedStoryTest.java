package model.story;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SavedStoryTest {

    @Test
    void testConstructorAndDefaults() {
        Character c = new Character("Luna");
        World w = new World("Valley");
        Scene s = new Scene("Start", new Choice("A", "Go"), new Choice("B", "Stay"));

        SavedStory story = new SavedStory(
                "My Adventure",
                "Fantasy",
                c,
                w,
                List.of(s),
                "{\"length\":\"medium\"}"
        );

        assertEquals("My Adventure", story.getTitle());
        assertEquals("Fantasy", story.getGenre());
        assertEquals(1, story.getTotalChapters());
        assertEquals(c, story.getCharacter());
        assertEquals(w, story.getWorld());
    }

    @Test
    void testDisplayTitleFallback() {
        SavedStory story = new SavedStory();
        story.setCharacter(new Character("Aiden"));
        story.setWorld(new World("Eclipse Plains"));

        assertEquals("Aiden's Adventure in Eclipse Plains", story.getDisplayTitle());
    }

    @Test
    void testShortSummary() {
        Scene s = new Scene("This is a long long long scene text for testing summary extraction.", null, null);
        SavedStory story = new SavedStory();
        story.setScenes(List.of(s));

        assertTrue(story.getShortSummary().length() > 10);
    }
}
