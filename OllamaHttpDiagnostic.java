import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class OllamaHttpDiagnostic {
    public static void main(String[] args) throws Exception {
        String body = "{\"model\":\"qwen3.5:9b\",\"prompt\":\"Reply with the single word READY.\",\"stream\":false}";
        HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:11434/api/generate"))
                .timeout(Duration.ofSeconds(300))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        long start = System.nanoTime();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("HTTP status: " + response.statusCode());
        System.out.println("Elapsed seconds: " + (System.nanoTime() - start) / 1_000_000_000.0);
        System.out.println("Response: " + response.body());
    }
}
