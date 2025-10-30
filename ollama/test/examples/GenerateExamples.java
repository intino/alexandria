package examples;

import io.intino.alexandria.ollama.Ollama;
import io.intino.alexandria.ollama.OllamaAPIException;
import io.intino.alexandria.ollama.requests.OllamaGenerateRequest;

public class GenerateExamples {

	private static final String LLM = "qwen3:4b-thinking";

	public static void main(String[] args) throws OllamaAPIException {
		generate();
		generateStream();
		generateWithOptions();
	}

	private static void generate() throws OllamaAPIException {
		Ollama ollama = Ollama.newClient();

		ollama.pullIfNotExists(LLM);

		var response = ollama.generate(LLM, "Hello, what is an int in Java?");

		System.out.println(response.thinking());
		System.out.println(response.text());
	}

	private static void generateStream() throws OllamaAPIException {
		Ollama ollama = Ollama.newClient();

		ollama.pullIfNotExists(LLM);

		var response = ollama.generateStream(LLM, "Hello, what is an int in Java?");

		for(var part : response) {
			System.out.print(part.text());
		}
	}

	private static void generateWithOptions() throws OllamaAPIException {
		Ollama ollama = Ollama.newClient();

		ollama.pullIfNotExists(LLM);

		var response = ollama.generate(new OllamaGenerateRequest()
				.model(LLM)
				.temperature(0.1)
				.numCtx(1024)
				.seed(0)
				.system("You are a helpful assistant, and you always respond in a friendly way")
				.prompt("""
                        Explain this code:
						
                        private Instant dueDate(String dueDate) {
                            return Timetag.of(dueDate).datetime().toInstant(ZoneOffset.UTC);
                        }
                        """)
		);

		System.out.println(response.text());
	}
}
