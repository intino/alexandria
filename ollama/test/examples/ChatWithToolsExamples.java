package examples;

import io.intino.alexandria.Json;
import io.intino.alexandria.ollama.Ollama;
import io.intino.alexandria.ollama.OllamaAPIException;
import io.intino.alexandria.ollama.OllamaMessage;
import io.intino.alexandria.ollama.OllamaMessage.Role;
import io.intino.alexandria.ollama.requests.OllamaChatRequest;
import io.intino.alexandria.ollama.tools.OllamaFunction;
import io.intino.alexandria.ollama.tools.OllamaToolCall;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Callable;

public class ChatWithToolsExamples {

	/**
	 * <p>You must use a LLM with tools (function calling) support, like llama3.1 or mistral-nemo. If not, ollama will throw an error.
	 * Keep in mind that when one or multiple functions are specified, the LLM will tend to always respond with a function call,
	 * even if a function call is optional and not mandatory.</p>
	 *
	 * <p>Tool calls are only supported in non-streaming mode. If you try to do so with chatStream, the LLM will try to
	 * specify the tool calls in the message content, but it won't be parsed by the library as proper function calls.</p>
	 * */

	private static final String LLM = "llama3.1"; // llama3.1, mistral-nemo, firefunction-v2, mistral...

	public static void main(String[] args) throws Exception {
		chatWithTools();
		chatWithToolsAndBinding();
		chatWithMultipleTools();
	}

	private static void chatWithTools() throws OllamaAPIException {
		Ollama ollama = Ollama.newClient();

		ollama.pullIfNotExists(LLM);

		var response = ollama.chat(new OllamaChatRequest()
				.model(LLM)
				.temperature(0.01)
				.addMessage(Role.user, "What is the weather in Las Palmas and in Madrid right now?")
				.withFunction(new OllamaFunction()
						.name("get_weather")
						.description("Get the weather of one or multiple cities at a certain datetime")
						.parameter("cities", "[string]", "The cities to query")
						.parameter("datetime", "string", "The datetime in yyyy-MM-dd HH:mm format to query." +
								" If not set, the current datetime will be used", false)
				)
		);

		OllamaMessage message = response.message();
		if(message.hasToolCalls()) {
			System.out.println(Json.toJsonPretty(message.toolCalls()));
		} else {
			System.out.println(message.content());
		}

		System.out.println(response.tokensPerSecond());
	}

	/**
	 * Bindings are user-defined Java objects that represents an ollama tool function
	 * You can use them to bind the LLM function call to a Java object and work with it
	 * */
	private static void chatWithToolsAndBinding() throws Exception {
		Ollama ollama = Ollama.newClient();

		ollama.pullIfNotExists(LLM);

		var response = ollama.chat(new OllamaChatRequest()
						.model(LLM)
						.temperature(0.01)
						.addMessage(Role.user, "What was the weather in Las Palmas and in Madrid the day 07/08/2024 at night?")
						// OllamaFunction.from(Class<?>) method parses the binding class and fill in all the ollama function arguments.
						.withFunction(OllamaFunction.from(GetWeather.class))
				// If you specify the binding after creating the OllamaFunction, then it
				// only sets the binding class, and the rest of arguments must be specified manually.
				//.withFunction(new OllamaFunction().name("get_weather").description("").parameter("cities", ...).binding(GetWeather.class))
		);

		OllamaMessage message = response.message();

		if(message.hasToolCalls()) {
			OllamaToolCall toolCall = message.toolCalls().getFirst();

			GetWeather binding = toolCall.function().binding();

			System.out.println(binding.cities);
			System.out.println(binding.datetime);
			binding.execute();

		} else {
			System.out.println(message.content());
		}

		System.out.println(response.tokensPerSecond());
	}

	private static void chatWithMultipleTools() throws Exception {
		Ollama ollama = Ollama.newClient();

		ollama.pullIfNotExists(LLM);

		@OllamaFunction.Binding
		class Sum implements Callable<Float> {
			@OllamaFunction.Param
			float x;
			@OllamaFunction.Param
			float y;

			public Float call() {
				return x + y;
			}
		}

		@OllamaFunction.Binding
		class Multiply implements Callable<Float> {
			@OllamaFunction.Param
			float x;
			@OllamaFunction.Param
			float y;

			public Float call() {
				return x * y;
			}
		}

		float result = 34938.1415f * 824394930.4838f;

		var response = ollama.chat(new OllamaChatRequest()
				.model(LLM)
				.temperature(0.01)
				.addMessage(Role.user, "What is 34938.1415f * 824394930.4838f?")
				.withFunction(OllamaFunction.from(Multiply.class))
				.withFunction(OllamaFunction.from(Sum.class))
		);

		OllamaMessage message = response.message();

		if(message.hasToolCalls()) {
			OllamaToolCall toolCall = message.toolCalls().getFirst();

			Callable<Number> binding = toolCall.function().binding();

			System.out.println("result of " + toolCall.function().name() +  " = " + binding.call());

		} else {
			System.out.println(message.content());
		}
		System.out.println("expected result = " + result);

		System.out.println(response.tokensPerSecond());
	}

	// Define a Java binding for an ollama tool function
	@OllamaFunction.Binding(name="get_weather", description = "Gets the weather of one or multiple cities in a given datetime")
	private static class GetWeather {

		@OllamaFunction.Param(description = "List of cities to query")
		private List<String> cities;

		@OllamaFunction.Param(required = false, description = """
		The datetime to query in format yyyy-MM-dd HH:mm. If not set, then the current datetime will be used.
		""")
		private String datetime;

		// ...

		// Optional
		public void execute() {
			if(datetime == null) {
				datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
			}
			System.out.println("Weather in " + cities + " at " + datetime + " is " + Math.random());
		}
	}
}
