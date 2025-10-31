/**
 * RomanceStrategy implements AIStrategy and provides a warm, emotional,
 * relationship-focused narrative structure.
 *
 * Person C (AI Integration):
 *  - Will replace all PLACEHOLDER TEXT with OpenAI-generated romantic prose.
 *  - Will build prompts using Character traits, World atmosphere, and chapter progression.
 *
 * The Strategy defines *meaning* behind choices:
 *  - Choice "A" typically represents vulnerability, openness, emotional risk.
 *  - Choice "B" typically represents cautious distance, emotional protection.
 */
package model.strategy;
import model.story.Character;
import model.story.World;
import model.story.Scene;
import model.story.Choice;
import model.story.StoryState;

public class RomanceStrategy implements AIStrategy {

    @Override
    public Scene generateIntro(Character character, World world) {
        // ===== PLACEHOLDER INTRO TEXT =====
        String text = character.getName() +
                " arrives in " + world.getLocation() + ", a place known for " + world.getHistory() + ". " +
                "There is a quiet softness in the air, something familiar yet unspoken. " +
                "Even though \"" + world.getRule() + "\", a gentle pull suggests that something meaningful could happen here.";
        // ===== PLACEHOLDER INTRO TEXT =====
        Choice choiceA = new Choice("A", "Let your guard down and follow the feeling.");
        Choice choiceB = new Choice("B", "Compose yourself and remain cautious.");

        return new Scene(text, choiceA, choiceB);
    }

    @Override
    public Scene generateNext(Choice choice, StoryState state) {
        String text;

        if (choice.getId().equals("A")) {
            // ===== PLACEHOLDER TEXT FOR PATH A =====
            text = "You choose to trust the moment. Something inside you opens, softly but unmistakably. " +
                    "There is a warmth here — a promise of connection. " +
                    "Chapter " + state.getChapter() + " deepens the bond.";
        } else {
            // ===== PLACEHOLDER TEXT FOR PATH B =====
            text = "You take a breath and hold back. Distance feels safer, but something in the silence lingers. " +
                    "The possibility of closeness remains, waiting. " +
                    "Chapter " + state.getChapter() + " continues with quiet tension.";
        }

        Choice nextA = new Choice("A", "Lean into the feeling.");
        Choice nextB = new Choice("B", "Protect yourself and retreat emotionally.");

        return new Scene(text, nextA, nextB);
    }
}
