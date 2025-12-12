package model.image;

import model.story.SceneModel;

/**
 * SceneMomentPromptFactory
 *
 * Produces prompts for a single moment from the current chapter.
 */
public class SceneMomentPromptFactory implements ImagePromptFactory {

    @Override
    public String buildImagePrompt(ImagePromptContext context) {

        StringBuilder sb = new StringBuilder();

        sb.append("Illustration of a key moment from chapter ")
                .append(context.getChapter())
                .append(" of a ")
                .append(context.getGenre() != null ? context.getGenre() : "fantasy")
                .append(" story. ");

        SceneModel scene = context.getScene();
        if (scene != null && scene.getStoryText() != null) {
            String text = scene.getStoryText().trim();
            if (text.length() > 350) {
                text = text.substring(0, 350) + "...";
            }

            sb.append("Scene description: ").append(text).append(" ");
        }

        sb.append("Use a visual style that matches the writing style: ")
                .append(context.getStyle() != null ? context.getStyle() : "descriptive")
                .append(". ")
                .append("No text or captions in the image.");

        return sb.toString();
    }
}
