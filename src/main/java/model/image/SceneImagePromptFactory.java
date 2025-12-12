package model.image;

import model.story.CharacterModel;
import model.story.SceneModel;
import model.story.WorldModel;

/**
 * SceneImagePromptFactory
 *
 * FACTORY IMPLEMENTATION:
 *   Builds a detailed image prompt for a single story moment
 *   (the current scene).
 *
 *   Uses:
 *     - Genre
 *     - Style (e.g., Descriptive, Neutral)
 *     - Chapter number
 *     - Character (name + traits)
 *     - World (location, rule, history)
 *     - Scene text (short summary)
 */
public class SceneImagePromptFactory implements ImagePromptFactory {

    @Override
    public String buildImagePrompt(ImagePromptContext ctx) {

        StringBuilder sb = new StringBuilder();

        String genre = safe(ctx.getGenre());
        String style = safe(ctx.getStyle());
        int chapter = ctx.getChapter();

        CharacterModel ch = ctx.getCharacter();
        WorldModel world = ctx.getWorld();
        SceneModel scene = ctx.getScene();

        /* -------------------------
           HEADER + GLOBAL STYLE
           ------------------------- */
        sb.append("Illustration for an interactive ")
                .append(genre.isEmpty() ? "story" : genre.toLowerCase() + " story")
                .append(". ");

        sb.append("High quality, cinematic, ").append(style.toLowerCase()).append(" style. ");
        sb.append("Single key frame from chapter ").append(chapter).append(". ");

        /* -------------------------
           CHARACTER DETAILS
           ------------------------- */
        if (ch != null) {
            sb.append("Main character: ")
                    .append(safe(ch.getName()))
                    .append(". Traits: ");
            if (ch.getTraits() != null && !ch.getTraits().isEmpty()) {
                sb.append(String.join(", ", ch.getTraits()));
            } else {
                sb.append("unspecified");
            }
            sb.append(". ");
        }

        /* -------------------------
           WORLD / LOCATION
           ------------------------- */
        if (world != null) {
            if (world.getLocation() != null && !world.getLocation().isBlank()) {
                sb.append("Setting: ").append(world.getLocation()).append(". ");
            }
            if (world.getRule() != null && !world.getRule().isBlank()) {
                sb.append("World rule: ").append(world.getRule()).append(". ");
            }
            if (world.getHistory() != null && !world.getHistory().isBlank()) {
                sb.append("World history hint: ").append(world.getHistory()).append(". ");
            }
        }

        /* -------------------------
           SCENE MOMENT DESCRIPTION
           ------------------------- */
        if (scene != null && scene.getStoryText() != null) {
            String text = scene.getStoryText().trim();

            // shorten long text
            if (text.length() > 280) {
                text = text.substring(0, 280) + "...";
            }

            sb.append("Capture the current moment where: ")
                    .append(text)
                    .append(" ");
        }

        /* -------------------------
           COMPOSITION HINTS
           ------------------------- */
        sb.append("Focus on the main character and the environment. ")
                .append("No text, no UI, no speech bubbles. ")
                .append("Rich lighting, clear silhouettes, strong mood.");

        return sb.toString();
    }

    /** Helper to avoid null strings */
    private String safe(String s) {
        return (s == null) ? "" : s.trim();
    }
}
