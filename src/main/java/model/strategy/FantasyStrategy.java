/**
 * FantasyStrategy implements AIStrategy for a fantasy style narrative.
 *
 * Person B (UI Developer):
 *  - Displays the Scene returned by this strategy.
 *  - Shows sceneText to the user and presents choiceA / choiceB as UI buttons.
 *  - When the user selects a choice, the UI passes that selection back to the Controller.
 *
 * Person C (AI / Controller Developer):
 *  - Will replace PLACEHOLDER TEXT with AI-generated content.
 *  - Builds prompts using:
 *        - character (name, traits, backstory)
 *        - world (location, rule, history)
 *        - state (chapter progression)
 *        - choice (branch direction)
 *  - Will call something like:
 *        OpenAIService.generateText(prompt)
 *  - No branching logic or Scene construction needs to change — only the text.
 *
 * Purpose:
 *  - This class defines how the story *progresses* (structure), not the final prose.
 *  - UI can fully function now with placeholder text.
 *  - AI integration can be added later without rewriting the story logic or UI code.
 */
package model.strategy;
import model.story.Character;
import model.story.World;
import model.story.Scene;
import model.story.Choice;
import model.story.StoryState;

public class FantasyStrategy implements AIStrategy {

    @Override
    public Scene generateIntro(Character character, World world) {
        // ===== PLACEHOLDER STORY TEXT =====
        String text = character.getName() + " steps into the " + world.getLocation() +
                ", a place whispered about in ancient tales. " +
                "The air shimmers with faint magic — even though " + world.getRule() + ". " +
                "Legends of the land speak of " + world.getHistory() + ".";
        // ===== PLACEHOLDER INTRO TEXT =====
        Choice choiceA = new Choice("A", "Follow a faint glowing trail through the trees");
        Choice choiceB = new Choice("B", "Call out to see if someone — or something — answers");

        return new Scene(text, choiceA, choiceB);
    }

    @Override
    public Scene generateNext(Choice choice, StoryState state) {
        // This will be replaced by AI, but branching structure remains the same.
        String text;

        if (choice.getId().equals("A")) {
            // ===== PLACEHOLDER TEXT FOR PATH A =====
            text = "Drawn to the mysterious glow, you move deeper into the forest. " +
                    "The magic grows stronger with every step. Chapter " + state.getChapter() + " begins.";
        } else {
            // ===== PLACEHOLDER TEXT FOR PATH B =====
            text = "Your voice echoes into the quiet woods. Something stirs. " +
                    "An ancient presence has heard you. Chapter " + state.getChapter() + " begins.";
        }
        // Choices remain UI-side. Only the text changes later.
        Choice nextA = new Choice("A", "Continue forward");
        Choice nextB = new Choice("B", "Retreat and reconsider");

        return new Scene(text, nextA, nextB);
    }
}
