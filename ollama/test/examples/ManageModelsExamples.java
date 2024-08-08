package examples;

import io.intino.alexandria.ollama.ModelFile;
import io.intino.alexandria.ollama.Ollama;
import io.intino.alexandria.ollama.OllamaAPIException;
import io.intino.alexandria.ollama.responses.OllamaListResponse;
import io.intino.alexandria.ollama.responses.OllamaPsResponse;
import io.intino.alexandria.ollama.responses.OllamaShowResponse;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.intino.alexandria.ollama.responses.OllamaListResponse.OllamaModel;

public class ManageModelsExamples {

	private static void listModels() throws OllamaAPIException {
		Ollama ollama = Ollama.newClient();

		OllamaListResponse response = ollama.list();
		Set<String> modelNames = response.modelNames(); // model names (:latest is omitted)
		List<OllamaModel> models = response.models();
		// ...
	}

	private static void pull() throws OllamaAPIException {
		Ollama ollama = Ollama.newClient();

		ollama.pull("llama3");

		ollama.pullIfNotExists("llama3");

		// Use this to monitor the pulling progress
		ollama.pullStream("llama3");
	}

	private static void showModel() throws OllamaAPIException {
		Ollama ollama = Ollama.newClient();

		OllamaShowResponse response = ollama.show("llama3", true);

		Map<String, Object> modelInfo = response.modelInfo();
		OllamaShowResponse.Details details = response.details();
		String modelfile = response.modelfile();
		//...
	}

	private static void deleteModel() throws OllamaAPIException {
		Ollama ollama = Ollama.newClient();

		ollama.delete("my_assistant");

		ollama.deleteIfExists("my_assistant");
	}

	private static void createModel() throws OllamaAPIException {
		Ollama ollama = Ollama.newClient();

		// You can specify the modelfile string directly
//		ollama.create("my_assistant", """
//				FROM llama3
//
//				PARAMETER temperature 0.9
//				PARAMETER num_ctx 4096
//
//				SYSTEM "You are Atri, a helpful assistant. You always respond with emojis."
//				""");

		// Or you can use a ModelFile object
		ollama.newClient("my_assistant", new ModelFile()
				.from("llama3")
				.temperature(0.9)
				.numCtx(4096)
				.adapters("/server/path/to/lora")
				.system("You are Atri, a helpful assistant. You always respond with emojis.")
		);

		// ollama.generate("my_assistant", "hello");
		// ...
	}

	private static void ps() throws OllamaAPIException {
		Ollama ollama = Ollama.newClient();

		// Get the current loaded models and its cpu/gpu usage
		OllamaPsResponse ps = ollama.ps();

		for(var model : ps.models()) {
			System.out.println(model.model() + ", memory = " + model.sizeVRAM()/1024.0f/1024.0f/1024.0f + " GB");
		}
	}
}
