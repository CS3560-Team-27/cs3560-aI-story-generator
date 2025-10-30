package model.story;

public class StoryState {
    private int chapter;

    public StoryState() {
        this.chapter = 1; // story starts at chapter 1
    }

    public int getChapter() {
        return chapter;
    }

    public void nextChapter() {
        chapter++;
    }
}
