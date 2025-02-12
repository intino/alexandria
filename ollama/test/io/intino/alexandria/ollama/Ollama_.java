package io.intino.alexandria.ollama;

import io.intino.alexandria.Json;
import io.intino.alexandria.ollama.requests.OllamaCreateModelRequest;

public class Ollama_ {

    public static void main(String[] args) throws OllamaAPIException {
        Ollama ollama = Ollama.newClient();

        ollama.deleteIfExists("myassistant");

		OllamaCreateModelRequest request = new OllamaCreateModelRequest()
				.model("myassistant")
				.from("qwen2.5:7b-instruct-q4_K_M")
				.temperature(0.21)
				.numCtx(1024)
				.system("You are a helpful assistant, and your name is Atri. You always include emojis in your responses.");

		System.out.println(Json.toJsonPretty(request));

		ollama.createModel(request);

        var response = ollama.generateStream("myassistant", "hello, what is a Deque in Java?");

        String fullText = response.processAllParts(p -> System.out.print(p.text()));
    }
}
