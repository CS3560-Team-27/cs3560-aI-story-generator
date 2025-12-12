package model.image;

import model.story.CharacterModel;
import model.story.WorldModel;

/**
 * CoverArtPromptFactory
 *
 * Builds a highly stylized, cinematic COVER ART prompt.
 * This image represents the ENTIRE story, not a single chapter.
 *
 * Includes:
 *   - Genre cues (Fantasy → enchanted forests, Sci-Fi → neon, etc.)
 *   - Character identity and traits
 *   - World tone + location
 *   - Symbolic elements that represent the journey
 */
public class CoverArtPromptFactory implements ImagePromptFactory {

    @Override
    public String buildImagePrompt(ImagePromptContext ctx) {

        StringBuilder sb = new StringBuilder();

        String genre = safe(ctx.getGenre());
        String style = safe(ctx.getStyle());

        CharacterModel ch = ctx.getCharacter();
        WorldModel world = ctx.getWorld();

        /* -----------------------------------------------------------
           COVER ART OVERVIEW
           ----------------------------------------------------------- */
        sb.append("Design a dramatic cinematic cover illustration for a ")
                .append(genre.isEmpty() ? "fiction story" : genre + " story")
                .append(". ");

        sb.append("Art style: ").append(style.toLowerCase())
                .append(", high detail, emotionally expressive, no text. ");

        /* -----------------------------------------------------------
           CHARACTER (HERO) SECTION
           ----------------------------------------------------------- */
        if (ch != null) {
            sb.append("Main hero: ").append(safe(ch.getName())).append(". ");
            if (ch.getTraits() != null && !ch.getTraits().isEmpty()) {
                sb.append("Personality traits: ")
                        .append(String.join(", ", ch.getTraits()))
                        .append(". ");
            }
        }

        /* -----------------------------------------------------------
           WORLD + TONE
           ----------------------------------------------------------- */
        if (world != null) {
            if (world.getLocation() != null && !world.getLocation().isBlank()) {
                sb.append("Primary setting: ").append(world.getLocation()).append(". ");
            }
            if (world.getRule() != null && !world.getRule().isBlank()) {
                sb.append("Key world rule: ").append(world.getRule()).append(". ");
            }
            if (world.getHistory() != null && !world.getHistory().isBlank()) {
                sb.append("World backstory elements: ").append(world.getHistory()).append(". ");
            }
        }

        /* -----------------------------------------------------------
           SYMBOLIC COMPONENTS
           ----------------------------------------------------------- */
        sb.append("Include symbolic elements representing the hero's journey, conflict, and theme. ");
        sb.append("Epic lighting, strong composition, wide angle, atmospheric background. ");

        return sb.toString();
    }

    private String safe(String s) {
        return (s == null) ? "" : s.trim();
    }
}
