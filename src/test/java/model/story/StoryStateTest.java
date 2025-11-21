package model.story;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StoryStateTest {

    @Test
    void testInitialChapter() {
        StoryState s = new StoryState();
        assertEquals(1, s.getChapter());
    }

    @Test
    void testNextChapter() {
        StoryState s = new StoryState();
        s.nextChapter();
        s.nextChapter();

        assertEquals(3, s.getChapter());
    }
}
