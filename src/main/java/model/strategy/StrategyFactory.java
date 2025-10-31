package model.strategy;

public class StrategyFactory {

    /**
     * Returns the appropriate AIStrategy implementation based on the given genre string.
     * Person C will call this when the player selects a genre in the UI.
     *
     * Example:
     *    AIStrategy strategy = StrategyFactory.create("Fantasy");
     *    strategy.generateIntro(character, world);
     *
     * As more genre strategies are added (Sci-Fi, Mystery, etc.), simply extend this method.
     */
    public static AIStrategy create(String genre) {
        switch (genre.toLowerCase()) {
            case "fantasy":
                return new FantasyStrategy();
            case "sci-fi":
                return new SciFiStrategy();
            case "mystery":
                return new MysteryStrategy();
            case "romance":
                return new RomanceStrategy();
            case "horror":
                return new HorrorStrategy();
            default:
                throw new IllegalArgumentException("Unknown genre: " + genre);
        }
    }
}
