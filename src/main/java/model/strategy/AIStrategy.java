package model.strategy;
import model.story.Character;
import model.story.World;
import model.story.Scene;
import model.story.Choice;
import model.story.StoryState;

public interface AIStrategy {
    Scene generateIntro(Character character, World world);
    Scene generateNext(Choice choice, StoryState state);
}
