/**
 * StoryRecord stores a single playthrough (or saved story) for the Library.
 *
 * Person B (UI Developer):
 *  - Displays StoryRecord information in the Library / Saved Stories screen.
 *  - Shows summary details such as character name, world location, and chapters played.
 *  - Allows the user to select, view, or favorite a saved story.
 *
 * Person C (AI & Persistence Developer):
 *  - Will add methods to convert StoryRecord to and from JSON.
 *  - Will implement saving StoryRecords to disk or cloud storage.
 *  - May add metadata (timestamps, duration, genre label, etc.).
 *  - Will handle loading StoryRecords back into StoryModel for replay or review.
 *
 * Purpose:
 *  - Stores the results of an entire played story session.
 *  - Contains character, world, scene history, tags, and favorite status.
 *  - Designed to be simple data storage that both UI and persistence systems can use.
 */
package model.library;
import model.story.Character;
import model.story.World;
import model.story.Scene;
import java.util.List;

public class StoryRecord {

    private Character character;
    private World world;
    private List<Scene> scenes; // full playthrough history
    private boolean favorite;
    private List<String> tags;   // Optional category labels

    public StoryRecord(Character character, World world, List<Scene> scenes) {
        this.character = character;
        this.world = world;
        this.scenes = scenes;
        this.favorite = false;
        this.tags = null;
    }

    public Character getCharacter() {
        return character;
    }

    public World getWorld() {
        return world;
    }

    public List<Scene> getScenes() {
        return scenes;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
