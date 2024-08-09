package ollamagal2;

import io.intino.alexandria.ollama.Ollama;
import io.intino.alexandria.ollama.OllamaAPIException;
import io.intino.alexandria.ollamagal2.OllamaGal2Client;

public class OllamaGal2_ {

	public static void main(String[] args) throws OllamaAPIException, InterruptedException {
		Ollama ollama = new OllamaGal2Client("<gal2-url>",
				"<ollama-gal2-model>",
				"<namespace>",
				"<token>");

		System.out.println(ollama.list().modelNames());

		var response = ollama.generateStream("llama3", "explain kubernetes in detail");

		for(var chunk : response) {
			System.out.print(chunk.response());
		}
	}
}
