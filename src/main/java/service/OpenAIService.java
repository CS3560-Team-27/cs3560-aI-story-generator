package service;

import java.net.http.HttpResponse;
import model.story.Scene;
import model.story.Choice;
import model.OpenAIClient;

// communication with OpenAI's API, Generates story text using the Chat Completions endpoint
public class OpenAIService {
    private static final String CHAT_URL = "https://api.openai.com/v1/chat/completions";
    private final String model;
    private static long lastRequestTime = 0;


    public OpenAIService() {
        String env = System.getenv("OPENAI_TEXT_MODEL");
        this.model = (env != null && !env.isEmpty()) ? env : "gpt-4o-mini";
    }

    /**
     * Generates a complete scene with story text and choices using OpenAI's API
     * @param prompt The story prompt requesting scene with choices
     * @return Scene object with AI-generated text and choices
     */
    public Scene generateSceneWithChoices(String prompt) throws Exception {
        int maxRetries = 5;
        long backoff = 1000; // start: 1 sec
        Exception last = null;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                System.out.println("[OpenAI] Attempt " + attempt);

                String response = tryChat(prompt);
                if (response != null && !response.isBlank()) {
                    return parseSceneAndChoices(response);
                }
            } catch (Exception e) {
                last = e;
                System.err.println("[OpenAI] Error: " + e.getMessage());
            }

            // exponential backoff
            if (attempt < maxRetries) {
                System.out.println("[OpenAI] Waiting " + backoff + "ms before retry...");
                Thread.sleep(backoff);
                backoff *= 2;  // 1s → 2s → 4s → 8s → 16s
            }
        }

        System.err.println("[OpenAI] Giving up after retries.");
        return createFallbackScene(last);
    }


    //Creates a fallback scene when AI fails to respond
    private Scene createFallbackScene(Exception error) {
        String fallbackText = "The story continues as you find yourself at a crossroads. " +
                             "The path ahead is uncertain, but you must make a choice to proceed. " +
                             "Your next decision will shape the direction of your journey.";
        
        Choice choiceA = new Choice("A", "Take the path that seems safer and more familiar");
        Choice choiceB = new Choice("B", "Choose the unknown path that promises adventure");
        
        System.out.println("[OpenAI] Created fallback scene due to: " + 
                         (error != null ? error.getMessage() : "No response from AI"));
        
        return new Scene(fallbackText, choiceA, choiceB);
    }

    /**
     * Parses AI response containing scene text and choices
     * Expected format:
     * SCENE: [scene text]
     * CHOICE_A: [choice A text] 
     * CHOICE_B: [choice B text]
     */
    private Scene parseSceneAndChoices(String aiResponse) {
        String sceneText = "";
        String choiceAText = "Continue";
        String choiceBText = "Try something else";
        
        try {
            // Extract scene text and choices using more robust parsing
            String[] sections = extractSections(aiResponse);
            sceneText = sections[0];
            choiceAText = sections[1];
            choiceBText = sections[2];
            
            // Validate that we have meaningful content
            if (sceneText.isEmpty() || sceneText.length() < 20) {
                sceneText = aiResponse;
            }
            
            // Ensure we always have two distinct choices
            if (choiceAText.isEmpty() || choiceAText.equals("Continue")) {
                choiceAText = generateFallbackChoiceA(sceneText);
            }
            if (choiceBText.isEmpty() || choiceBText.equals("Try something else")) {
                choiceBText = generateFallbackChoiceB(sceneText);
            }
            
            // Ensure choices are different and not empty
            if (choiceAText.equals(choiceBText)) choiceBText = "Take a different approach";
            if (choiceAText.trim().isEmpty()) choiceAText = "Continue forward";
            if (choiceBText.trim().isEmpty()) choiceBText = "Try another way";
            
        } catch (Exception e) {
            System.err.println("[OpenAI] Failed to parse scene response: " + e.getMessage());
            sceneText = aiResponse;
            choiceAText = generateFallbackChoiceA(sceneText);
            choiceBText = generateFallbackChoiceB(sceneText);
        }
        
        Choice choiceA = new Choice("A", choiceAText);
        Choice choiceB = new Choice("B", choiceBText);
        
        return new Scene(sceneText, choiceA, choiceB);
    }
    
    //Extracts scene text and choices from AI response using multiple parsing strategies
    private String[] extractSections(String response) {
        String sceneText = "";
        String choiceA = "Continue";
        String choiceB = "Try something else";
        
        // Strategy 1: Look for structured format (SCENE:, CHOICE_A:, CHOICE_B:)
        String[] lines = response.split("\n");
        StringBuilder sceneBuilder = new StringBuilder();
        boolean inScene = false;
        
        for (String line : lines) {
            String trimmed = line.trim();
            
            if (trimmed.startsWith("SCENE:")) {
                sceneText = trimmed.substring(6).trim();
                inScene = true;
                continue;
            } else if (trimmed.startsWith("CHOICE_A:") || trimmed.startsWith("CHOICE A:")) {
                choiceA = trimmed.substring(trimmed.indexOf(':') + 1).trim();
                inScene = false;
                continue;
            } else if (trimmed.startsWith("CHOICE_B:") || trimmed.startsWith("CHOICE B:")) {
                choiceB = trimmed.substring(trimmed.indexOf(':') + 1).trim();
                inScene = false;
                continue;
            }
            
            // If we're in scene mode and haven't found a choice yet, add to scene
            if (inScene || (sceneText.isEmpty() && !trimmed.isEmpty() && !trimmed.contains("CHOICE"))) {
                if (sceneBuilder.length() > 0) sceneBuilder.append("\n");
                sceneBuilder.append(line);
            }
        }
        
        // If we didn't find structured format, try other approaches
        if (sceneText.isEmpty() && sceneBuilder.length() > 0) {
            sceneText = sceneBuilder.toString().trim();
        }
        
        // Strategy 2: Look for numbered/bulleted choices if we still have defaults
        if (choiceA.equals("Continue") || choiceB.equals("Try something else")) {
            String[] choices = {sceneText, choiceA, choiceB};
            extractNumberedChoices(response, choices);
            extractBulletChoices(response, choices);
            choiceA = choices[1];
            choiceB = choices[2];
        }
        
        return new String[]{sceneText, choiceA, choiceB};
    }
    
    private void extractNumberedChoices(String response, String[] choices) {
        String[] lines = response.split("\n");
        for (String line : lines) {
            String trimmed = line.trim();
            // Check for numbered or lettered choices
            if (trimmed.matches("^[1aA]\\..*") || trimmed.matches("^[1aA]\\).*")) {
                choices[1] = trimmed.substring(2).trim();
            } else if (trimmed.matches("^[2bB]\\..*") || trimmed.matches("^[2bB]\\).*")) {
                choices[2] = trimmed.substring(2).trim();
            }
        }
    }
    
    private void extractBulletChoices(String response, String[] choices) {
        String[] lines = response.split("\n");
        boolean foundFirst = false;
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("- ") || trimmed.startsWith("* ")) {
                if (!foundFirst) {
                    choices[1] = trimmed.substring(2).trim();
                    foundFirst = true;
                } else {
                    choices[2] = trimmed.substring(2).trim();
                    break;
                }
            }
        }
    }
    
    //Generate context-appropriate fallback choices based on scene content
    private String generateFallbackChoiceA(String sceneText) {
        String lower = sceneText.toLowerCase();
        if (lower.contains("danger") || lower.contains("threat")) return "Face the danger head-on";
        if (lower.contains("mystery") || lower.contains("strange")) return "Investigate further";
        if (lower.contains("door") || lower.contains("path")) return "Go forward";
        return "Take action";
    }
    
    private String generateFallbackChoiceB(String sceneText) {
        String lower = sceneText.toLowerCase();
        if (lower.contains("danger") || lower.contains("threat")) return "Find another way";
        if (lower.contains("mystery") || lower.contains("strange")) return "Proceed with caution";
        if (lower.contains("door") || lower.contains("path")) return "Look around first";
        return "Wait and observe";
    }

    // ==================== OpenAI API Communication ====================
    
    //Calls OpenAI Chat Completions API to generate story text
    private String tryChat(String prompt) throws Exception {
        String systemMessage = "You are a skilled interactive storyteller. " +
                              "Always respond in the exact format requested. " +
                              "When asked for scene with choices, provide exactly one SCENE section and exactly two CHOICE sections (CHOICE_A and CHOICE_B). " +
                              "Never omit any requested sections.";
        
        String payload = "{"
                + "\"model\":\"" + model + "\","
                + "\"messages\":["
                + "{\"role\":\"system\",\"content\":\"" + esc(systemMessage) + "\"},"
                + "{\"role\":\"user\",\"content\":\"" + esc(prompt) + "\"}"
                + "],"
                + "\"max_tokens\":1500,"
                + "\"temperature\":0.7,"
                + "\"presence_penalty\":0.1,"
                + "\"frequency_penalty\":0.1"
                + "}";
        
        HttpResponse<String> res = OpenAIClient.getInstance().postJson(CHAT_URL, payload);
        
        if (res.statusCode() == 429) {
            throw new Exception("Rate limit exceeded, will retry");
        }
        
        if (res.statusCode() / 100 != 2) {
            throw new Exception("OpenAI API error: HTTP " + res.statusCode() + ": " + clip(res.body()));
        }
        
        return parseContent(res.body());
    }

    //Extracts the generated content from OpenAI's JSON response
    private String parseContent(String body) {
        try {
            // Look for "choices":[{"message":{"content":"..."}}]
            int contentIdx = body.indexOf("\"content\"", body.indexOf("\"choices\""));
            if (contentIdx == -1) return null;
            
            int startQuote = body.indexOf('"', contentIdx + 9);
            if (startQuote == -1) return null;
            
            int endQuote = startQuote + 1;
            while (endQuote < body.length() && !(body.charAt(endQuote) == '"' && body.charAt(endQuote - 1) != '\\')) {
                endQuote++;
            }
            
            if (endQuote >= body.length()) return null;
            
            return unesc(body.substring(startQuote + 1, endQuote));
        } catch (Exception e) {
            System.err.println("[OpenAI] Failed to parse response: " + e.getMessage());
            return null;
        }
    }

    // ==================== Helper Methods ====================
    
    // Escapes special characters for JSON
    private static String esc(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"")
                                 .replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    }
    
    // Unescapes JSON characters back to normal text
    private static String unesc(String s) {
        return s == null ? "" : s.replace("\\n", "\n").replace("\\r", "\r").replace("\\t", "\t")
                                 .replace("\\\"", "\"").replace("\\\\", "\\");
    }

    // Clips long strings for logging
    private static String clip(String s) {
        return s == null ? "null" : (s.length() > 500 ? s.substring(0, 500) + "..." : s);
    }
}
