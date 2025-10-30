package io.intino.alexandria.ollama.requests;

import com.google.gson.annotations.SerializedName;
import io.intino.alexandria.ollama.OllamaMessage;
import io.intino.alexandria.ollama.OllamaParameters;
import io.intino.alexandria.ollama.requests.OllamaRequest.WithKeepAlive;
import io.intino.alexandria.ollama.requests.OllamaRequest.WithOptions;
import io.intino.alexandria.ollama.tools.OllamaFunction;
import io.intino.alexandria.ollama.tools.OllamaTool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class OllamaChatRequest extends WithOptions<OllamaChatRequest> implements WithKeepAlive<OllamaChatRequest>, OllamaParameters<OllamaChatRequest> {

	private String model;
	private List<OllamaMessage> messages;
	private List<OllamaTool> tools;
	private String format;
	private Boolean think;
	private boolean stream;
	@SerializedName("keep_alive")
	private String keepAlive;

	public String model() {
		return model;
	}

	public OllamaChatRequest model(String model) {
		this.model = model;
		return this;
	}

	public List<OllamaMessage> messages() {
		return messages;
	}

	public OllamaChatRequest addMessage(OllamaMessage.Role role, String content) {
		return addMessage(new OllamaMessage(role, content));
	}

	public OllamaChatRequest addMessage(OllamaMessage message) {
		if(messages == null) messages = new ArrayList<>();
		messages.add(message);
		return this;
	}

	public OllamaChatRequest messages(Collection<OllamaMessage> messages) {
		this.messages = messages == null ? null : new ArrayList<>(messages);
		return this;
	}

	public OllamaChatRequest messages(OllamaMessage... messages) {
		this.messages = messages == null ? null : Arrays.asList(messages);
		return this;
	}

	public List<OllamaTool> tools() {
		return tools;
	}

	public OllamaChatRequest tools(Collection<OllamaTool> tools) {
		this.tools = tools == null ? null : new ArrayList<>(tools);
		return this;
	}

	public OllamaChatRequest tools(OllamaTool... tools) {
		this.tools = tools == null ? null : Arrays.asList(tools);
		return this;
	}

	public OllamaChatRequest withFunction(OllamaFunction function) {
		if(tools == null) tools = new ArrayList<>(1);
		tools.add(OllamaTool.of(function));
		return this;
	}

	public String format() {
		return format;
	}

	public OllamaChatRequest format(String format) {
		this.format = format;
		return this;
	}

	public Boolean think() {
		return think;
	}

	public OllamaChatRequest think(Boolean think) {
		this.think = think;
		return this;
	}

	public boolean stream() {
		return stream;
	}

	public OllamaChatRequest stream(boolean stream) {
		this.stream = stream;
		return this;
	}

	@Override
	public String keepAlive() {
		return keepAlive;
	}

	@Override
	public OllamaChatRequest keepAlive(String keepAlive) {
		this.keepAlive = keepAlive;
		return this;
	}

	@Override
	public OllamaChatRequest self() {
		return this;
	}
}
