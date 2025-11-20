package model.story;
import java.util.ArrayList;
import java.util.List;

public class Character {
    private String name;
    private List<String> traits;
    private String backstory;

    // Main constructor - uses defaults for missing values
    public Character(String name, List<String> traits, String backstory) {
        this.name = name != null ? name : "";
        this.traits = traits != null ? traits : new ArrayList<>();
        this.backstory = backstory != null ? backstory : "Unknown";
    }

    // Convenience constructors delegate to main constructor
    public Character(String name) {
        this(name, null, null);
    }

    public Character(String name, List<String> traits) {
        this(name, traits, null);
    }

    // Getters + Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTraits() {
        return traits;
    }

    public void setTraits(List<String> traits) {
        this.traits = traits;
    }

    public String getBackstory() {
        return backstory;
    }

    public void setBackstory(String backstory) {
        this.backstory = backstory;
    }
}
