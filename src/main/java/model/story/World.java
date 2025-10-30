package model.story;

public class World {
    private String location;
    private String rule;
    private String history;

    // Constructor: location only
    public World(String location) {
        this.location = location;
        this.rule = "No rules";
        this.history = "An ancient war scarred the land";
    }

    // Constructor: location + rule
    public World(String location, String rule) {
        this.location = location;
        this.rule = rule;
        this.history = "An ancient war scarred the land";
    }

    // Constructor: full world details
    public World(String location, String rule, String history) {
        this.location = location;
        this.rule = rule;
        this.history = history;
    }

    // Getters + Setters
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }
}
