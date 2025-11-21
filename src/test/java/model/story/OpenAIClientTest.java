package model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class OpenAIClientTest {

    @Test
    void testSingletonInstance() {
        OpenAIClient a = OpenAIClient.getInstance();
        OpenAIClient b = OpenAIClient.getInstance();

        assertSame(a, b, "OpenAIClient must be a Singleton");
    }

    @Test
    void testApiKeyMissingThrowsError() {
        // Because OpenAIClient reads env var ONCE,
        // we fake it by checking constructor behavior via reflection if needed.

        assertThrows(IllegalStateException.class, () -> {
            var constructor = OpenAIClient.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();  // Will fail if no API key exists
        });
    }
}
