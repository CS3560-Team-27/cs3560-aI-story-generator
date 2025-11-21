package model.story;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StoryStateExtraTest {

    @Test
    void testMultipleNextChapters() {
        StoryState s = new StoryState();

        for (int i = 0; i < 50; i++) {
            s.nextChapter();
        }

        assertEquals(51, s.getChapter());
    }

    @Test
    void testChapterNotNegative() {
        StoryState s = new StoryState();
        assertTrue(s.getChapter() > 0);
    }
}
