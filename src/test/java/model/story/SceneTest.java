package model.story;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SceneTest {

    @Test
    void testConstructor() {
        Choice a = new Choice("A", "Left");
        Choice b = new Choice("B", "Right");

        Scene s = new Scene("Welcome", a, b);

        assertEquals("Welcome", s.getSceneText());
        assertEquals(a, s.getChoiceA());
        assertEquals(b, s.getChoiceB());
    }

    @Test
    void testSetters() {
        Scene s = new Scene("Text", null, null);

        s.setSceneText("Updated");
        s.setChoiceA(new Choice("A", "Go"));
        s.setChoiceB(new Choice("B", "Stay"));

        assertEquals("Updated", s.getSceneText());
        assertEquals("Go", s.getChoiceA().getText());
        assertEquals("Stay", s.getChoiceB().getText());
    }
}
