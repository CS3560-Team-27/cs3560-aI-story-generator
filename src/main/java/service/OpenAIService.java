package service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.OpenAIClient;
import model.story.ChoiceModel;
import model.story.SceneModel;

/**
 * OpenAIService — Centralized OpenAI API Access
 * Supports:
 *  - Story generation
 *  - Image generation (URL-based)
 */
public class OpenAIService {

    private final OpenAIClient client = OpenAIClient.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();

    /* =========================================================
       STORY GENERATION
       ========================================================= */

    public SceneModel generateScene(String prompt) throws Exception {
        String raw = client.sendRequest(prompt);
        return parseOpenAIResponse(raw);
    }

    private SceneModel parseOpenAIResponse(String raw) throws Exception {

        JsonNode root = mapper.readTree(raw);

        JsonNode choicesNode = root.get("choices");
        if (choicesNode == null || !choicesNode.isArray() || choicesNode.size() == 0) {
            throw new Exception("OpenAI returned no choices:\n" + raw);
        }

        JsonNode message = choicesNode.get(0).get("message");
        if (message == null || message.get("content") == null) {
            throw new Exception("Missing assistant message content.\n" + raw);
        }

        String content = message.get("content").asText();
        JsonNode storyJson = mapper.readTree(content);

        require(storyJson, "story");
        require(storyJson, "isEnding");

        String storyText = storyJson.get("story").asText("");
        boolean isEnding = storyJson.get("isEnding").asBoolean(false);

        if (isEnding) {
            ChoiceModel A = new ChoiceModel("A", "The End");
            ChoiceModel B = new ChoiceModel("B", "The End");
            ChoiceModel C = new ChoiceModel("C", "The End");
            return new SceneModel(storyText, A, B, C, true);
        }

        require(storyJson, "choices");
        JsonNode choiceJson = storyJson.get("choices");

        require(choiceJson, "A");
        require(choiceJson, "B");
        require(choiceJson, "C");

        ChoiceModel A = new ChoiceModel("A", choiceJson.get("A").asText(""));
        ChoiceModel B = new ChoiceModel("B", choiceJson.get("B").asText(""));
        ChoiceModel C = new ChoiceModel("C", choiceJson.get("C").asText(""));

        return new SceneModel(storyText, A, B, C, false);
    }

    private void require(JsonNode node, String fieldName) throws Exception {
        if (node == null || node.get(fieldName) == null || node.get(fieldName).isNull()) {
            throw new Exception("Missing required field \"" + fieldName + "\" in story JSON.");
        }
    }

    /* =========================================================
       IMAGE GENERATION (URL-BASED)
       ========================================================= */

    public String generateImageURL(String prompt) throws Exception {

        String json = client.generateImage(prompt);

        JsonNode root = mapper.readTree(json);
        JsonNode data = root.get("data");

        if (data == null || !data.isArray() || data.size() == 0) {
            throw new Exception("OpenAI image response missing data field.");
        }

        return data.get(0).get("url").asText();
    }
}
