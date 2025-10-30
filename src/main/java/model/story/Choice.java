package model.story;

public class Choice {
    private String id;
    private String text;

    public Choice(String id, String text) {
        this.id = id;       // "A" or "B"
        this.text = text;   // What the player sees (the narration)
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
