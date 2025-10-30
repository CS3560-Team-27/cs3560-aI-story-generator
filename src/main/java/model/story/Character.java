package model.story;

import java.util.ArrayList;
import java.util.List;

public class Character {
    private String name;
    private List<String> traits;
    private String backstory;

    // Constructor: name only
    public Character(String name) {
        this.name = name;
        this.traits = new ArrayList<String>(); // empty list by default
        this.backstory = "Unknown";
    }

    // Constructor: name + traits
    public Character(String name, List<String> traits) {
        this.name = name;
        this.traits = traits;
        this.backstory = "Unknown";
    }

    // Constructor: name + traits + backstory
    public Character(String name, List<String> traits, String backstory) {
        this.name = name;
        this.traits = traits;
        this.backstory = backstory;
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
