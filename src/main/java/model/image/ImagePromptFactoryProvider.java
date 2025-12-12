package model.image;

/**
 * ImagePromptFactoryProvider
 *
 * FACTORY SELECTOR:
 * Returns the correct ImagePromptFactory implementation
 * based on the request type.
 */
public class ImagePromptFactoryProvider {

    public ImagePromptFactory getFactory(ImageRequestType type) {

        return switch (type) {
            case SCENE_MOMENT -> new SceneImagePromptFactory();
            case COVER_ART -> new CoverArtPromptFactory();
        };
    }
}
