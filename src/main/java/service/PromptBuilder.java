package service;

import model.story.Character;
import model.story.World;
import model.story.Scene;
import model.story.Choice;
import model.story.StoryState;

// PromptBuilder - Creates prompts for AI story generation
public class PromptBuilder {

    // Creates a prompt for the story's opening scene with choices
    public static String introWithChoices(Character c, World w,
                               String genre, String length, String complexity, String style) {
        String traits = String.join(", ", c.getTraits() == null ? java.util.List.of() : c.getTraits());
        return "You are an expert storyteller. Write the opening scene for a "
                + genre + " interactive story.\n"
                + "Character: " + nz(c.getName()) + "; Traits: " + traits + "; Backstory: " + nz(c.getBackstory()) + ".\n"
                + "World: location " + nz(w.getLocation()) + "; rule " + nz(w.getRule()) + "; history " + nz(w.getHistory()) + ".\n"
                + "Length: " + length + ". Style: " + style + ". Audience: " + complexity + ".\n\n"
                + "CRITICAL FORMATTING REQUIREMENT: You must format your response EXACTLY as shown below. Do not deviate from this format:\n\n"
                + "SCENE: [Write 2-3 engaging paragraphs of story text here]\n\n"
                + "CHOICE_A: [First meaningful choice option]\n"
                + "CHOICE_B: [Second meaningful choice option that leads to a different story path]\n\n"
                + "EXAMPLE:\n"
                + "SCENE: The ancient forest whispers secrets as you approach the towering oak tree. Mysterious symbols glow faintly on its bark, pulsing with an otherworldly energy. You feel drawn to touch them, but something deep within warns you of unknown consequences.\n\n"
                + "CHOICE_A: Touch the glowing symbols on the tree\n"
                + "CHOICE_B: Step back and examine the area for other clues\n\n"
                + "Remember: Always provide exactly two distinct choices labeled CHOICE_A and CHOICE_B.";
    }

    // Creates a prompt for continuing the story with new choices based on player choice
    public static String nextWithChoices(Scene previous, Choice chosen, String genre, String length, String complexity, String style, StoryState state) {
        return "Continue the " + genre + " interactive story.\n"
                + "Previous scene: " + nz(previous.getSceneText()) + "\n"
                + "The player chose: " + chosen.getId() + ") " + nz(chosen.getText()) + ".\n"
                + "Length: " + length + ". Style: " + style + ". Audience: " + complexity + ".\n\n"
                + "CRITICAL FORMATTING REQUIREMENT: You must format your response EXACTLY as shown below. Do not deviate from this format:\n\n"
                + "SCENE: [Write 2-3 engaging paragraphs showing what happens next based on the player's choice. This is chapter " + state.getChapter() + "]\n\n"
                + "CHOICE_A: [First meaningful choice for what to do next]\n"
                + "CHOICE_B: [Second meaningful choice for what to do next]\n\n"
                + "EXAMPLE:\n"
                + "SCENE: Your decision leads to unexpected consequences. The path forward becomes clearer as new challenges emerge, testing your resolve in ways you hadn't anticipated.\n\n"
                + "CHOICE_A: Face the challenge directly\n"
                + "CHOICE_B: Look for an alternative approach\n\n"
                + "Remember: The two choices must be distinct and meaningful, leading to different story directions. Always provide exactly two choices labeled CHOICE_A and CHOICE_B.";
    }

    // Null-safe string helper - returns "(unspecified)" for null/blank strings
    private static String nz(String s) { 
        return (s == null || s.isBlank()) ? "(unspecified)" : s; 
    }
}
