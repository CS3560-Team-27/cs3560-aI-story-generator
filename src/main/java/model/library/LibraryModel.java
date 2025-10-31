/**
 * LibraryModel represents the user's collection of saved story playthroughs.
 *
 * Person B (UI Developer):
 *  - Displays the list of saved stories in a Library or Saved Stories screen.
 *  - Allows users to select, favorite, or delete stories.
 *  - Calls LibraryModel methods (addStory, removeStory, getFavorites, etc.) through the Controller.
 *
 * Person C (AI & Persistence Developer):
 *  - Will implement saving the LibraryModel to JSON (or database/cloud).
 *  - Will load StoryRecords back into memory when the app starts or when requested.
 *  - May add file I/O methods or connect this model to a SaveLoadController.
 *
 * Purpose:
 *  - Stores StoryRecord objects in-memory.
 *  - Provides access to the full list or filtered favorites.
 *  - Does not perform any file saving, network sync, or UI rendering directly.
 */
package model.library;
import java.util.ArrayList;
import java.util.List;

public class LibraryModel {

    private List<StoryRecord> savedStories;

    public LibraryModel() {
        this.savedStories = new ArrayList<>();
    }

    public void addStory(StoryRecord record) {
        savedStories.add(record);
    }

    public List<StoryRecord> getAllStories() {
        return savedStories;
    }

    public List<StoryRecord> getFavorites() {
        List<StoryRecord> favorites = new ArrayList<>();
        for (StoryRecord record : savedStories) {
            if (record.isFavorite()) {
                favorites.add(record);
            }
        }
        return favorites;
    }

    public void removeStory(StoryRecord record) {
        savedStories.remove(record);
    }
}
