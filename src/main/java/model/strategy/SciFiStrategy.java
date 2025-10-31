package model.strategy;

import model.story.Character;
import model.story.World;
import model.story.Scene;
import model.story.Choice;
import model.story.StoryState;

/**
 * SciFiStrategy implements AIStrategy and defines how a Space Opera style story
 * should progress in tone and branching.
 *
 * Person C will later replace the placeholder text with AI-generated content,
 * using prompts built from Character, World, and StoryState to maintain tone.
 */
public class SciFiStrategy implements AIStrategy {

    @Override
    public Scene generateIntro(Character character, World world) {
        // ===== PLACEHOLDER STORY TEXT =====
        String text = character.getName() +
                " arrives at the " + world.getLocation() + ", a massive frontier outpost at the edge of known space. " +
                "Stories speak of " + world.getHistory() + ", but few have returned to confirm it. " +
                "Despite the directive that \"" + world.getRule() + "\", something hums beneath the station’s power grid. " +
                "A signal. Ancient. Intelligent.";
        // ===== PLACEHOLDER INTRO TEXT =====
        Choice choiceA = new Choice("A", "Follow the origin of the signal deep into the station.");
        Choice choiceB = new Choice("B", "Open a transmission channel and attempt to communicate.");

        return new Scene(text, choiceA, choiceB);
    }

    @Override
    public Scene generateNext(Choice choice, StoryState state) {
        // This will be replaced by AI, but branching structure remains the same.
        String text;

        if (choice.getId().equals("A")) {
            // ===== PLACEHOLDER TEXT FOR PATH A =====
            text = "You step into the restricted sectors. Lights flicker. Gravity fluctuates. " +
                    "Whatever is generating the signal is powerful — and aware of you. " +
                    "Chapter " + state.getChapter() + " unfolds among the humming corridors.";
        } else {
            // ===== PLACEHOLDER TEXT FOR PATH B =====
            text = "Your transmission echoes into the void. A voice replies — layered, ancient, and vast. " +
                    "\"You are not the first to seek us,\" it says. " +
                    "Chapter " + state.getChapter() + " begins with the universe watching.";
        }
        // Choices remain UI-side. Only the text changes later.
        Choice nextA = new Choice("A", "Push forward into the unknown.");
        Choice nextB = new Choice("B", "Pause and analyze the situation.");

        return new Scene(text, nextA, nextB);
    }
}
