/**
 * MysteryStrategy implements AIStrategy and structures a slow-reveal, investigative narrative tone.
 *
 * Person C (AI & Controller Developer):
 *  - Will replace the PLACEHOLDER TEXT sections below with AI-generated narrative.
 *  - Will build prompts from Character, World, StoryState, and the selected Choice.
 *
 * This class’s job is to define *story flow* — not prose.
 * The prose will later be generated through OpenAIService inside the same methods.
 */
package model.strategy;
import model.story.Character;
import model.story.World;
import model.story.Scene;
import model.story.Choice;
import model.story.StoryState;

public class MysteryStrategy implements AIStrategy {

    @Override
    public Scene generateIntro(Character character, World world) {

        // ===== PLACEHOLDER INTRO TEXT =====
        String text = character.getName() +
                " arrives in " + world.getLocation() + ", where people rarely speak of " + world.getHistory() + ". " +
                "Something feels wrong — the silence, the watchful eyes, the way the air seems to hold its breath. " +
                "Even though \"" + world.getRule() + "\", the truth lies hidden just beneath the surface.";
        // ===== PLACEHOLDER INTRO TEXT =====
        Choice choiceA = new Choice("A", "Investigate quietly and observe the surroundings.");
        Choice choiceB = new Choice("B", "Ask locals direct questions about what's going on.");

        return new Scene(text, choiceA, choiceB);
    }

    @Override
    public Scene generateNext(Choice choice, StoryState state) {
        String text;

        if (choice.getId().equals("A")) {
            // ===== PLACEHOLDER TEXT FOR PATH A =====
            text = "You choose discretion. Footsteps echo softly as you explore. A shadow flickers in your peripheral vision. " +
                    "Someone — or something — is aware of you. " +
                    "Chapter " + state.getChapter() + " draws you deeper into the unseen.";

        } else {

            // ===== PLACEHOLDER TEXT FOR PATH B =====
            text = "You press the locals with questions. Their eyes dart nervously. A single phrase slips out: " +
                    "\"It's watching.\" " +
                    "Chapter " + state.getChapter() + " begins — and the truth is closing in.";

        }

        Choice nextA = new Choice("A", "Follow the clues further.");
        Choice nextB = new Choice("B", "Retreat and rethink what you've learned.");

        return new Scene(text, nextA, nextB);
    }
}
