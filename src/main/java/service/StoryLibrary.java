package service;

import model.story.SavedStory;
import model.story.StoryModel;
import model.story.Character;
import model.story.World;
import model.story.Scene;
import model.story.Choice;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// StoryLibrary - Handles saving and loading stories to/from the filesystem
public class StoryLibrary {
    private static final String SAVE_DIRECTORY = "saves";
    private static StoryLibrary instance;
    
    private final Path saveDir;
    private List<SavedStory> savedStories;

    private StoryLibrary() {
        this.saveDir = Paths.get(SAVE_DIRECTORY);
        this.savedStories = new ArrayList<>();
        
        initializeLibrary();
    }

    public static synchronized StoryLibrary getInstance() {
        if (instance == null) {
            instance = new StoryLibrary();
        }
        return instance;
    }

    // Initialize the library by creating directories and loading existing stories
    private void initializeLibrary() {
        try {
            // Create save directory if it doesn't exist
            if (!Files.exists(saveDir)) {
                Files.createDirectories(saveDir);
                System.out.println("[Library] Created save directory: " + saveDir.toAbsolutePath());
            }
            
            // Load existing stories
            loadLibraryIndex();
            
        } catch (IOException e) {
            System.err.println("[Library] Failed to initialize library: " + e.getMessage());
        }
    }

    // Save a story to the library
    public SavedStory saveStory(StoryModel model, String genre, String title, 
                               String length, String complexity, String style) throws IOException {
        
        // Create story configuration
        String config = String.format(
            "length=%s;complexity=%s;style=%s", 
            length, complexity, style
        );
        
        // Create SavedStory object
        SavedStory savedStory = new SavedStory(
            title != null && !title.trim().isEmpty() ? title.trim() : null,
            genre,
            model.getCharacter(),
            model.getWorld(),
            new ArrayList<>(model.getAllScenes()),
            config
        );
        
        // Save individual story file
        String fileName = savedStory.getId() + ".txt";
        Path storyFile = saveDir.resolve(fileName);
        
        try {
            saveStoryToFile(savedStory, storyFile);
            System.out.println("[Library] Saved story to: " + storyFile.toAbsolutePath());
            
            // Add to in-memory list and update index
            savedStories.add(savedStory);
            saveLibraryIndex();
            
            return savedStory;
            
        } catch (IOException e) {
            System.err.println("[Library] Failed to save story: " + e.getMessage());
            throw new IOException("Failed to save story: " + e.getMessage(), e);
        }
    }

    // Load all saved stories
    public List<SavedStory> getAllSavedStories() {
        return new ArrayList<>(savedStories);
    }

    // Load a specific story by ID
    public SavedStory loadStory(String storyId) throws IOException {
        String fileName = storyId + ".txt";
        Path storyFile = saveDir.resolve(fileName);
        
        if (!Files.exists(storyFile)) {
            throw new IOException("Story file not found: " + fileName);
        }
        
        try {
            return loadStoryFromFile(storyFile);
        } catch (IOException e) {
            System.err.println("[Library] Failed to load story " + storyId + ": " + e.getMessage());
            throw new IOException("Failed to load story: " + e.getMessage(), e);
        }
    }

    // Delete a story from the library
    public boolean deleteStory(String storyId) {
        try {
            String fileName = storyId + ".txt";
            Path storyFile = saveDir.resolve(fileName);
            
            if (Files.exists(storyFile)) {
                Files.delete(storyFile);
                System.out.println("[Library] Deleted story file: " + fileName);
            }
            
            // Remove from in-memory list
            savedStories.removeIf(story -> story.getId().equals(storyId));
            saveLibraryIndex();
            
            return true;
            
        } catch (IOException e) {
            System.err.println("[Library] Failed to delete story " + storyId + ": " + e.getMessage());
            return false;
        }
    }

    // ==================== Private Methods ====================

    // Save story to file in simple text format
    private void saveStoryToFile(SavedStory story, Path filePath) throws IOException {
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(filePath))) {
            writer.println("=== STORY METADATA ===");
            writer.println("ID: " + story.getId());
            writer.println("TITLE: " + nullSafe(story.getTitle()));
            writer.println("GENRE: " + story.getGenre());
            writer.println("CREATED: " + story.getCreatedAt());
            writer.println("MODIFIED: " + story.getLastModified());
            writer.println("CHAPTERS: " + story.getTotalChapters());
            writer.println("CONFIG: " + story.getStoryConfiguration());
            
            writeCharacter(writer, story.getCharacter());
            writeWorld(writer, story.getWorld());
            writeScenes(writer, story.getScenes());
        }
    }
    
    private void writeCharacter(PrintWriter writer, Character c) {
        writer.println("\n=== CHARACTER ===");
        if (c != null) {
            writer.println("NAME: " + (c.getName() != null ? c.getName() : ""));
            writer.println("BACKSTORY: " + (c.getBackstory() != null ? c.getBackstory() : ""));
            writer.println("TRAITS: " + (c.getTraits() != null ? String.join(",", c.getTraits()) : ""));
        }
    }
    
    private void writeWorld(PrintWriter writer, World w) {
        writer.println("\n=== WORLD ===");
        if (w != null) {
            writer.println("LOCATION: " + (w.getLocation() != null ? w.getLocation() : ""));
            writer.println("RULE: " + (w.getRule() != null ? w.getRule() : ""));
            writer.println("HISTORY: " + (w.getHistory() != null ? w.getHistory() : ""));
        }
    }
    
    private void writeScenes(PrintWriter writer, List<Scene> scenes) {
        writer.println("\n=== SCENES ===");
        if (scenes != null) {
            for (int i = 0; i < scenes.size(); i++) {
                Scene scene = scenes.get(i);
                writer.println("SCENE_" + i + "_TEXT: " + scene.getSceneText());
                if (scene.getChoiceA() != null) {
                    writer.println("SCENE_" + i + "_CHOICE_A: " + scene.getChoiceA().getText());
                }
                if (scene.getChoiceB() != null) {
                    writer.println("SCENE_" + i + "_CHOICE_B: " + scene.getChoiceB().getText());
                }
                writer.println();
            }
        }
    }
    
    private String nullSafe(String value) {
        return value != null ? value : "";
    }

    // Load story from file
    private SavedStory loadStoryFromFile(Path filePath) throws IOException {
        SavedStory story = new SavedStory();
        Character character = new Character("");
        World world = new World("");
        List<Scene> scenes = new ArrayList<>();
        
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            Scene currentScene = null;
            int currentSceneIndex = -1;
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                // Parse metadata
                parseMetadata(line, story);
                // Parse character
                parseCharacter(line, character);
                // Parse world
                parseWorld(line, world);
                // Parse scenes
                Object[] sceneResult = parseScene(line, currentScene, currentSceneIndex, scenes);
                currentScene = (Scene) sceneResult[0];
                currentSceneIndex = (Integer) sceneResult[1];
            }
            
            // Add the last scene
            if (currentScene != null) {
                scenes.add(currentScene);
            }
        }
        
        story.setCharacter(character);
        story.setWorld(world);
        story.setScenes(scenes);
        
        return story;
    }
    
    private void parseMetadata(String line, SavedStory story) {
        if (line.startsWith("ID: ")) {
            story.setId(line.substring(4));
        } else if (line.startsWith("TITLE: ")) {
            String title = line.substring(7);
            if (!title.isEmpty()) story.setTitle(title);
        } else if (line.startsWith("GENRE: ")) {
            story.setGenre(line.substring(7));
        } else if (line.startsWith("CREATED: ")) {
            story.setCreatedAt(LocalDateTime.parse(line.substring(9)));
        } else if (line.startsWith("MODIFIED: ")) {
            story.setLastModified(LocalDateTime.parse(line.substring(10)));
        } else if (line.startsWith("CHAPTERS: ")) {
            story.setTotalChapters(Integer.parseInt(line.substring(10)));
        } else if (line.startsWith("CONFIG: ")) {
            story.setStoryConfiguration(line.substring(8));
        }
    }
    
    private void parseCharacter(String line, Character character) {
        if (line.startsWith("NAME: ")) {
            character.setName(line.substring(6));
        } else if (line.startsWith("BACKSTORY: ")) {
            character.setBackstory(line.substring(11));
        } else if (line.startsWith("TRAITS: ")) {
            String traits = line.substring(8);
            if (!traits.isEmpty()) {
                character.setTraits(List.of(traits.split(",")));
            }
        }
    }
    
    private void parseWorld(String line, World world) {
        if (line.startsWith("LOCATION: ")) {
            world.setLocation(line.substring(10));
        } else if (line.startsWith("RULE: ")) {
            world.setRule(line.substring(6));
        } else if (line.startsWith("HISTORY: ")) {
            world.setHistory(line.substring(9));
        }
    }
    
    private Object[] parseScene(String line, Scene currentScene, int currentSceneIndex, List<Scene> scenes) {
        if (line.startsWith("SCENE_") && line.contains("_TEXT: ")) {
            // New scene
            int sceneNum = extractSceneNumber(line);
            if (sceneNum != currentSceneIndex) {
                if (currentScene != null) {
                    scenes.add(currentScene);
                }
                currentScene = new Scene(line.substring(line.indexOf("_TEXT: ") + 7), 
                                        new Choice("A", "Continue"), 
                                        new Choice("B", "Try something else"));
                currentSceneIndex = sceneNum;
            }
        } else if (line.startsWith("SCENE_") && line.contains("_CHOICE_A: ")) {
            if (currentScene != null) {
                currentScene.setChoiceA(new Choice("A", line.substring(line.indexOf("_CHOICE_A: ") + 11)));
            }
        } else if (line.startsWith("SCENE_") && line.contains("_CHOICE_B: ")) {
            if (currentScene != null) {
                currentScene.setChoiceB(new Choice("B", line.substring(line.indexOf("_CHOICE_B: ") + 11)));
            }
        }
        return new Object[]{currentScene, currentSceneIndex};
    }

    private int extractSceneNumber(String line) {
        try {
            int start = line.indexOf("SCENE_") + 6;
            int end = line.indexOf("_", start);
            return Integer.parseInt(line.substring(start, end));
        } catch (Exception e) {
            return -1;
        }
    }

    // Load the library index (simplified version)
    private void loadLibraryIndex() {
        try {
            if (Files.exists(saveDir)) {
                Files.list(saveDir)
                    .filter(path -> path.toString().endsWith(".txt"))
                    .forEach(path -> {
                        try {
                            SavedStory story = loadStoryFromFile(path);
                            savedStories.add(story);
                        } catch (IOException e) {
                            System.err.println("[Library] Failed to load story from " + path + ": " + e.getMessage());
                        }
                    });
                
                System.out.println("[Library] Loaded " + savedStories.size() + " existing stories");
            }
        } catch (IOException e) {
            System.err.println("[Library] Failed to scan existing stories: " + e.getMessage());
        }
    }

    // Save the library index (simplified - just for logging)
    private void saveLibraryIndex() {
        System.out.println("[Library] Library now contains " + savedStories.size() + " stories");
    }
}