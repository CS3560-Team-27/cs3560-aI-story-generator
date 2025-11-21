package model.story;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SavedStoryExtraTest {

    @Test
    void testModifiedTimeUpdatesWhenTitleChanges() throws InterruptedException {
        SavedStory story = new SavedStory();
        var before = story.getLastModified();

        Thread.sleep(10);
        story.setTitle("New Title");

        assertTrue(story.getLastModified().isAfter(before));
    }

    @Test
    void testModifiedTimeUpdatesWhenScenesChange() throws InterruptedException {
        SavedStory story = new SavedStory();
        var before = story.getLastModified();

        Thread.sleep(10);
        story.setScenes(List.of(new Scene("x", null, null)));

        assertTrue(story.getLastModified().isAfter(before));
    }

    @Test
    void testToStringContainsGenreAndChapters() {
        SavedStory story = new SavedStory();
        story.setGenre("Fantasy");
        story.setScenes(List.of(new Scene("A", null, null)));

        String output = story.toString();

        assertTrue(output.contains("Fantasy"));
        assertTrue(output.contains("1"));
    }
}
