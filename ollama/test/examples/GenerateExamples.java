package examples;

import io.intino.alexandria.ollama.Ollama;
import io.intino.alexandria.ollama.OllamaAPIException;
import io.intino.alexandria.ollama.requests.OllamaGenerateRequest;

public class GenerateExamples {

	public static void main(String[] args) throws OllamaAPIException {
		generate();
		generateStream();
		generateWithOptions();
	}

	private static void generate() throws OllamaAPIException {
		Ollama ollama = Ollama.newClient();

		ollama.pullIfNotExists("llama3");

		var response = ollama.generate("llama3", "Hello, what is an int in Java?");

		System.out.println(response.text());
	}

	private static void generateStream() throws OllamaAPIException {
		Ollama ollama = Ollama.newClient();

		ollama.pullIfNotExists("llama3");

		var response = ollama.generateStream("llama3", "Hello, what is an int in Java?");

		for(var part : response) {
			System.out.print(part.text());
		}
	}

	private static void generateWithOptions() throws OllamaAPIException {
		Ollama ollama = Ollama.newClient();

		ollama.pullIfNotExists("llama3");

		var response = ollama.generate(new OllamaGenerateRequest()
				.model("llama3")
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
