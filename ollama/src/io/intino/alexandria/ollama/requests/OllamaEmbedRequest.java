package io.intino.alexandria.ollama.requests;

import com.google.gson.annotations.SerializedName;
import io.intino.alexandria.ollama.requests.OllamaRequest.WithKeepAlive;
import io.intino.alexandria.ollama.requests.OllamaRequest.WithOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class OllamaEmbedRequest extends WithOptions<OllamaEmbedRequest> implements WithKeepAlive<OllamaEmbedRequest> {

	private String model;
	private List<String> input;
	private boolean truncate = true;
	@SerializedName("keep_alive")
	private String keepAlive;

	public String model() {
		return model;
	}

	public OllamaEmbedRequest model(String model) {
		this.model = model;
		return this;
	}

	public List<String> input() {
		return input;
	}

	public OllamaEmbedRequest input(Collection<String> input) {
		this.input = input == null ? null : new ArrayList<>(input);
		return this;
	}

	public OllamaEmbedRequest input(String[] input) {
		this.input = input == null ? null : Arrays.asList(input);
		return this;
	}

	public OllamaEmbedRequest input(String first, String... other) {
		this.input = new ArrayList<>(1 + (other == null ? 0 : other.length));
		this.input.add(first);
		if(other != null) input.addAll(Arrays.asList(other));
		return this;
	}

	public boolean truncate() {
		return truncate;
	}

	public OllamaEmbedRequest truncate(boolean truncate) {
		this.truncate = truncate;
		return this;
	}

	@Override
	public String keepAlive() {
		return keepAlive;
	}

	@Override
	public OllamaEmbedRequest keepAlive(String keepAlive) {
		this.keepAlive = keepAlive;
		return this;
	}

	@Override
	public OllamaEmbedRequest self() {
		return this;
	}
}
