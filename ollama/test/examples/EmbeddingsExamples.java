package examples;

import io.intino.alexandria.ollama.Ollama;
import io.intino.alexandria.ollama.OllamaAPIException;
import io.intino.alexandria.ollama.requests.OllamaEmbedRequest;
import io.intino.alexandria.ollama.responses.OllamaEmbedResponse;

import java.util.Arrays;

public class EmbeddingsExamples {

	public static void main(String[] args) throws OllamaAPIException {
		createEmbeddings();
	}

	private static void createEmbeddings() throws OllamaAPIException {
		Ollama ollama = Ollama.newClient();

		ollama.pullIfNotExists("mxbai-embed-large");

		String text = "hello, this is a test of embeddings from Java using ollama";

		// Use embedFloats to get the embeddings as float vectors
		OllamaEmbedResponse.OfDouble response = ollama.embed(new OllamaEmbedRequest()
				.model("mxbai-embed-large")
				.input(text)
		);

		double[] embedding = response.embeddings()[0];
		//List<List<Double>> embeddingsAsLists = response.embeddingsAsLists();
		System.out.println(text + ": " + Arrays.toString(embedding));
	}
}
