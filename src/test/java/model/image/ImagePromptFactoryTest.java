// src/test/java/model/image/ImagePromptFactoryTest.java

package model.image;

import model.story.CharacterModel;
import model.story.SceneModel;
import model.story.WorldModel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ImagePromptFactoryTest {

    @Test
    void testCoverArtFactoryIncludesCharacterAndWorld() {

        CharacterModel c =
                new CharacterModel("Lyria", List.of("Brave"), "Backstory");

        WorldModel w =
                new WorldModel("Enchanted Forest", "Magic allowed", "Ancient war");

        ImagePromptContext ctx = new ImagePromptContext(
                "Fantasy",
                "Descriptive",
                1,
                c,
                w,
                null
        );

        ImagePromptFactory factory = new CoverArtPromptFactory();
        String prompt = factory.buildImagePrompt(ctx);

        assertNotNull(prompt);

        // Character included
        assertTrue(prompt.contains("Lyria"));

        // World included
        assertTrue(prompt.contains("Enchanted Forest"));

        // Cover intent (semantic, not brittle)
        assertTrue(prompt.toLowerCase().contains("cover"));
    }

    @Test
    void testFactoryProviderReturnsSceneFactory() {

        ImagePromptFactoryProvider provider =
                new ImagePromptFactoryProvider();

        ImagePromptFactory factory =
                provider.getFactory(ImageRequestType.SCENE_MOMENT);

        assertNotNull(factory);

        // Must match ACTUAL implementation
        assertTrue(factory instanceof SceneImagePromptFactory);
    }
}
