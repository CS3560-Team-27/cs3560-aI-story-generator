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
        CharacterModel c = new CharacterModel("Lyria", List.of("Brave"), "Backstory");
        WorldModel w = new WorldModel("Enchanted Forest", "Magic allowed", "Ancient war");
        SceneModel scene = null;

        ImagePromptContext ctx = new ImagePromptContext(
                "Fantasy",
                "Descriptive",
                1,
                c,
                w,
                scene
        );

        ImagePromptFactory factory = new CoverArtPromptFactory();
        String prompt = factory.buildImagePrompt(ctx);

        assertTrue(prompt.contains("Lyria"));
        assertTrue(prompt.contains("Enchanted Forest"));
        assertTrue(prompt.toLowerCase().contains("book cover"));
    }

    @Test
    void testFactoryProviderReturnsSceneFactory() {
        ImagePromptFactoryProvider provider = new ImagePromptFactoryProvider();
        ImagePromptFactory factory = provider.getFactory(ImageRequestType.SCENE_MOMENT);

        assertNotNull(factory);
        assertTrue(factory instanceof SceneMomentPromptFactory);
    }
}
