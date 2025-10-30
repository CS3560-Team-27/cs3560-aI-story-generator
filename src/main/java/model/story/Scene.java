package model.story;

public class Scene {
    private String sceneText;
    private Choice choiceA;
    private Choice choiceB;

    public Scene(String sceneText, Choice choiceA, Choice choiceB) {
        this.sceneText = sceneText;
        this.choiceA = choiceA;
        this.choiceB = choiceB;
    }

    // Getters and Setters
    public String getSceneText() {
        return sceneText;
    }

    public void setSceneText(String sceneText) {
        this.sceneText = sceneText;
    }

    public Choice getChoiceA() {
        return choiceA;
    }

    public void setChoiceA(Choice choiceA) {
        this.choiceA = choiceA;
    }

    public Choice getChoiceB() {
        return choiceB;
    }

    public void setChoiceB(Choice choiceB) {
        this.choiceB = choiceB;
    }
}
