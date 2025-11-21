package model.story;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CharacterTest {

    @Test
    void testMainConstructor() {
        List<String> traits = List.of("Brave", "Curious");
        Character c = new Character("Elowen", traits, "Exiled princess");

        assertEquals("Elowen", c.getName());
        assertEquals(traits, c.getTraits());
        assertEquals("Exiled princess", c.getBackstory());
    }

    @Test
    void testConstructorDefaults() {
        Character c = new Character("Elowen");

        assertEquals("Elowen", c.getName());
        assertNotNull(c.getTraits());
        assertTrue(c.getTraits().isEmpty());
        assertEquals("Unknown", c.getBackstory());
    }

    @Test
    void testSetters() {
        Character c = new Character("A");
        c.setName("B");
        c.setTraits(new ArrayList<>(List.of("Kind")));
        c.setBackstory("Heroic");

        assertEquals("B", c.getName());
        assertEquals(List.of("Kind"), c.getTraits());
        assertEquals("Heroic", c.getBackstory());
    }
}
