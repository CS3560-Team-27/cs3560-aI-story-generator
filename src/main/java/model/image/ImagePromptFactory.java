package model.image;

/**
 * ImagePromptFactory (Interface)
 *
 * FACTORY PATTERN:
 * Each concrete factory builds a different style
 * of image prompt.
 */
public interface ImagePromptFactory {

    String buildImagePrompt(ImagePromptContext context);
}
