package service;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Properties;

/**
 * OpenAIClient - Singleton HTTP client for OpenAI API communication
 * Handles API key loading from environment or config file
 */
public class OpenAIClient {
    private static OpenAIClient instance;
    private final HttpClient http;
    private final String apiKey;

    private OpenAIClient() {
        // Configure HTTP client with timeouts
        this.http = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(15))
                .build();
        
        // Load API key from environment or config file
        this.apiKey = resolveKey();
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("Missing OPENAI_API_KEY (env var or src/main/resources/config.properties)");
        }
    }

    public static synchronized OpenAIClient getInstance() {
        if (instance == null) instance = new OpenAIClient();
        return instance;
    }

    /**
     * Sends a POST request with JSON payload to OpenAI API
     */
    public HttpResponse<String> postJson(String url, String json) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(60))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        return http.send(req, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Resolves API key from environment variable or config file
     */
    private static String resolveKey() {
        // Try environment variable first
        String env = System.getenv("OPENAI_API_KEY");
        if (env != null && !env.isEmpty()) return env;

        // Fallback to config.properties file
        try (InputStream in = OpenAIClient.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) return null;
            Properties p = new Properties();
            p.load(in);
            return p.getProperty("OPENAI_API_KEY");
        } catch (Exception ignored) {}
        return null;
    }
}
