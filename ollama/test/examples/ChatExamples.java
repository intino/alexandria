package examples;

import io.intino.alexandria.ollama.Ollama;
import io.intino.alexandria.ollama.OllamaAPIException;
import io.intino.alexandria.ollama.OllamaMessage;
import io.intino.alexandria.ollama.OllamaMessage.Role;
import io.intino.alexandria.ollama.requests.OllamaChatRequest;

public class ChatExamples {

	public static void main(String[] args) throws OllamaAPIException {
		chat();
		chatStream();
		chatWithOptions();
	}

	private static void chat() throws OllamaAPIException {
		Ollama ollama = Ollama.newClient();

		ollama.pullIfNotExists("llama3");

		var response = ollama.chat("llama3",
				new OllamaMessage(Role.user, "Hello, who are the 2 best footballers of the recent decades?"),
				new OllamaMessage(Role.assistant, "Messi and Cristiano Ronaldo"),
				new OllamaMessage(Role.user, "Write a detailed comparison of both of them")
		);

		System.out.println(response.message().content());
		//System.out.println(response.text());
	}

	private static void chatStream() throws OllamaAPIException {
		Ollama ollama = Ollama.newClient();

		ollama.pullIfNotExists("llama3");

		var response = ollama.chatStream("llama3",
				new OllamaMessage(Role.user, "Hello, who are the 2 best footballers of the recent decades?"),
				new OllamaMessage(Role.assistant, "Messi and Cristiano Ronaldo"),
				new OllamaMessage(Role.user, "Write a detailed comparison of both of them")
		);

		for(var part : response) {
			System.out.print(part.message().content());
		}
	}

	private static void chatWithOptions() throws OllamaAPIException {
		Ollama ollama = Ollama.newClient();

		ollama.pullIfNotExists("llama3");

		var response = ollama.chat(new OllamaChatRequest()
				.model("llama3")
				.temperature(0.1)
				.numCtx(1024)
				.seed(0)
				.addMessage(Role.system, "You are a Java coding expert. You always respond in a professional way")
				.addMessage(Role.user, """
                        Explain this code:
						
                        private Instant dueDate(String dueDate) {
                            return Timetag.of(dueDate).datetime().toInstant(ZoneOffset.UTC);
                        }
                        """)
				.addMessage(Role.assistant, """
						This code in Java converts from a String to an Instant object.
						""")
				.addMessage(Role.user, "Why? What are the advantages of that?")
		);

		System.out.println(response.message().content());
	}
}
