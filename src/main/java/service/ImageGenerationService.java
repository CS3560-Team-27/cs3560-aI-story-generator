package service;

import model.image.*;
import model.story.StoryModel;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * ImageGenerationService
 *
 * Attempts real image generation first.
 * Falls back to placeholder image if:
 *  - Rate limit is hit
 *  - Network/API error occurs
 *
 * This ensures the UI never breaks.
 */
public class ImageGenerationService {

    private final ImagePromptFactoryProvider factoryProvider =
            new ImagePromptFactoryProvider();

    /** Toggle real image generation */
    private boolean useRealImages = true;

    /* =========================================================
       CONFIG
       ========================================================= */

    public void setUseRealImages(boolean enabled) {
        this.useRealImages = enabled;
    }

    /* =========================================================
       PROMPT CREATION (FACTORY PATTERN)
       ========================================================= */

    public String buildImagePromptForStory(StoryModel storyModel,
                                           String selectedStyle,
                                           ImageRequestType type) {

        ImagePromptFactory factory = factoryProvider.getFactory(type);

        ImagePromptContext context = new ImagePromptContext(
                storyModel.getGenre(),
                selectedStyle,
                storyModel.getState().getChapter(),
                storyModel.getCharacter(),
                storyModel.getWorld(),
                storyModel.getCurrentScene()
        );

        return factory.buildImagePrompt(context);
    }

    /* =========================================================
       IMAGE GENERATION WITH FALLBACK
       ========================================================= */

    public Image generateImageWithFallback(String prompt) {

        if (useRealImages) {
            try {
                // FUTURE: real API call goes here
                // return generateRealImage(prompt);

                throw new RuntimeException("Rate limit hit"); // simulate for now

            } catch (Exception ex) {
                System.err.println("[ImageGeneration] Falling back to placeholder:");
                System.err.println(ex.getMessage());
            }
        }

        return generatePlaceholderImage(prompt);
    }

    /* =========================================================
       PLACEHOLDER IMAGE (SAFE FALLBACK)
       ========================================================= */

    private Image generatePlaceholderImage(String prompt) {

        BufferedImage img = new BufferedImage(300, 400, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();

        g.setColor(new Color(225, 215, 205));
        g.fillRect(0, 0, 300, 400);

        g.setColor(Color.DARK_GRAY);
        g.setFont(new Font("SansSerif", Font.BOLD, 16));
        g.drawString("AI Illustration", 70, 175);

        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g.drawString("Placeholder image", 90, 200);

        g.dispose();
        return img;
    }
}
