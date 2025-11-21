package model.story;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChoiceTest {

    @Test
    void testConstructor() {
        Choice c = new Choice("A", "Open the door");

        assertEquals("A", c.getId());
        assertEquals("Open the door", c.getText());
    }

    @Test
    void testSetters() {
        Choice c = new Choice("A", "Test");
        c.setId("B");
        c.setText("Updated");

        assertEquals("B", c.getId());
        assertEquals("Updated", c.getText());
    }
}
