package io.intino.alexandria.ollama;

import java.io.IOException;

public class Ollama_ {

	public static void main(String[] args) throws OllamaAPIException, IOException {
		Ollama ollama = Ollama.newClient();

		ollama.deleteIfExists("myassistant");

		ollama.createModel("myassistant", new ModelFile()
				.from("llama3")
				.temperature(0.21)
				.numCtx(1024)
				.system("You are a helpful assistant, and your name is Atri. You always include emojis in your responses.")
		);

		var response = ollama.generateStream("myassistant", "hello, what is a Deque in Java?");

		for(var part : response) {
			System.out.print(part.text());
		}
	}
}
