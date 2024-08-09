package examples;

import io.intino.alexandria.ollama.Ollama;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class OllamaGeneralExamples {

	private static void createOllamaClient() {
		// Default
		Ollama ollama = Ollama.newClient();
		// Custom url
		ollama = Ollama.newClient("http://192.168.1.30:9192");
		// Custom settings
		ollama = Ollama.newClient("http://192.168.1.30:9192", Duration.of(1, ChronoUnit.HOURS));

		ollama.timeout(Duration.of(1, ChronoUnit.HOURS));

		ollama.setCommonHeader("Authorization", "<token>");

		ollama.close();
	}
}
