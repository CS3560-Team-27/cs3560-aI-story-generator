/**
 * HorrorStrategy implements AIStrategy and creates a tense, unsettling story tone.
 *
 * Person C (AI Integration Developer):
 *  - Will replace all PLACEHOLDER TEXT with OpenAI-generated horror prose.
 *  - Will build prompts using:
 *       Character traits, World atmosphere, StoryState progression, and player Choice.
 *
 * Key emotional meaning:
 *  - Choice "A" typically represents approaching or confronting the source of fear.
 *  - Choice "B" typically represents hesitation, caution, or retreat.
 */
package model.strategy;
import model.story.Character;
import model.story.World;
import model.story.Scene;
import model.story.Choice;
import model.story.StoryState;

public class HorrorStrategy implements AIStrategy {

    @Override
    public Scene generateIntro(Character character, World world) {
        // ===== PLACEHOLDER INTRO TEXT =====
        String text = character.getName() +
                " arrives in " + world.getLocation() + ". The locals speak of " + world.getHistory() + ", " +
                "but they lower their voices when they do. " +
                "The silence here is heavy — as if the air itself is waiting. " +
                "Even though \"" + world.getRule() + "\", something is watching.";
        // ===== PLACEHOLDER INTRO TEXT =====
        Choice choiceA = new Choice("A", "Step forward and investigate the unsettling presence.");
        Choice choiceB = new Choice("B", "Stay back and observe carefully, trying not to be noticed.");

        return new Scene(text, choiceA, choiceB);
    }

    @Override
    public Scene generateNext(Choice choice, StoryState state) {
        String text;

        if (choice.getId().equals("A")) {
            // ===== PLACEHOLDER TEXT FOR PATH A =====
            text = "You move closer. Footsteps echo — but they are not your own. " +
                    "A shadow stretches where no light should fall. " +
                    "Chapter " + state.getChapter() + " tightens its grip.";
        } else {
            // ===== PLACEHOLDER TEXT FOR PATH B =====
            text = "You hold still. The silence grows heavier. Your heartbeat becomes thunder in your ears. " +
                    "Something shifts just out of view. " +
                    "Chapter " + state.getChapter() + " waits in the dark.";

        }

        Choice nextA = new Choice("A", "Confront whatever is there.");
        Choice nextB = new Choice("B", "Retreat before it's too late.");

        return new Scene(text, nextA, nextB);
    }
}
