package model.image;

/**
 * ImageRequestType
 *
 * Specifies what type of image prompt should be generated.
 *
 * Example types:
 *   SCENE_MOMENT → describes a single moment inside the story
 *   COVER_ART     → describes a book-cover style full composition
 *
 * Used by ImagePromptFactoryProvider to select the correct factory.
 */
public enum ImageRequestType {
    SCENE_MOMENT,
    COVER_ART
}
