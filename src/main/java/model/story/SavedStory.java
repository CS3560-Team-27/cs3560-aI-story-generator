package model.story;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * SavedStory - Represents a complete story that has been saved to the library
 */
public class SavedStory {
    private String id;
    private String title;
    private String genre;
    private Character character;
    private World world;
    private List<Scene> scenes;
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;
    private int totalChapters;
    private String storyConfiguration; // JSON string with length, complexity, style

    public SavedStory() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
    }

    public SavedStory(String title, String genre, Character character, World world, 
                      List<Scene> scenes, String storyConfig) {
        this();
        this.title = title;
        this.genre = genre;
        this.character = character;
        this.world = world;
        this.scenes = scenes;
        this.totalChapters = scenes.size();
        this.storyConfiguration = storyConfig;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        updateModifiedTime();
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public List<Scene> getScenes() {
        return scenes;
    }

    public void setScenes(List<Scene> scenes) {
        this.scenes = scenes;
        this.totalChapters = scenes != null ? scenes.size() : 0;
        updateModifiedTime();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public int getTotalChapters() {
        return totalChapters;
    }

    public void setTotalChapters(int totalChapters) {
        this.totalChapters = totalChapters;
    }

    public String getStoryConfiguration() {
        return storyConfiguration;
    }

    public void setStoryConfiguration(String storyConfiguration) {
        this.storyConfiguration = storyConfiguration;
    }

    // Utility methods
    private void updateModifiedTime() {
        this.lastModified = LocalDateTime.now();
    }

    public String getFormattedCreatedDate() {
        return createdAt.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"));
    }

    public String getShortSummary() {
        if (scenes == null || scenes.isEmpty()) {
            return "No content";
        }
        
        String firstScene = scenes.get(0).getSceneText();
        if (firstScene.length() > 150) {
            return firstScene.substring(0, 150) + "...";
        }
        return firstScene;
    }

    /**
     * Generate a default title based on character and world if no title is set
     */
    public String getDisplayTitle() {
        if (title != null && !title.trim().isEmpty()) {
            return title;
        }
        
        // Generate title from character and world
        String charName = (character != null && character.getName() != null) ? 
                         character.getName() : "Hero";
        String worldLoc = (world != null && world.getLocation() != null) ? 
                         world.getLocation() : "Unknown Land";
        
        return charName + "'s Adventure in " + worldLoc;
    }

    @Override
    public String toString() {
        return "SavedStory{" +
                "title='" + getDisplayTitle() + '\'' +
                ", genre='" + genre + '\'' +
                ", chapters=" + totalChapters +
                ", created=" + getFormattedCreatedDate() +
                '}';
    }
}