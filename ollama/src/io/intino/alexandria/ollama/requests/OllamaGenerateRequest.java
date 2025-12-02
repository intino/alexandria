package io.intino.alexandria.ollama.requests;

import com.google.gson.annotations.SerializedName;
import io.intino.alexandria.Base64;
import io.intino.alexandria.ollama.requests.OllamaRequest.WithKeepAlive;
import io.intino.alexandria.ollama.requests.OllamaRequest.WithOptions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class OllamaGenerateRequest extends WithOptions<OllamaGenerateRequest> implements WithKeepAlive<OllamaGenerateRequest> {

	private String model;
	private String prompt;
	private String system;
	private Boolean raw;
	private String template;
	private String format;
	private int[] context;
	private Boolean think;
	private List<String> images;
	@SerializedName("keep_alive")
	private String keepAlive;
	private boolean stream;

	public String model() {
		return model;
	}

	public OllamaGenerateRequest model(String model) {
		this.model = model;
		return this;
	}

	public String prompt() {
		return prompt;
	}

	public OllamaGenerateRequest prompt(String prompt) {
		this.prompt = prompt;
		return this;
	}

	public String system() {
		return system;
	}

	public OllamaGenerateRequest system(String system) {
		this.system = system;
		return this;
	}

	public Boolean raw() {
		return raw;
	}

	public OllamaGenerateRequest raw(Boolean raw) {
		this.raw = raw;
		return this;
	}

	public String template() {
		return template;
	}

	public OllamaGenerateRequest template(String template) {
		this.template = template;
		return this;
	}

	public String format() {
		return format;
	}

	public OllamaGenerateRequest format(String format) {
		this.format = format;
		return this;
	}

	public int[] context() {
		return context;
	}

	public OllamaGenerateRequest context(int[] context) {
		this.context = context;
		return this;
	}

	public Boolean think() {
		return think;
	}

	public OllamaGenerateRequest think(Boolean think) {
		this.think = think;
		return this;
	}

	public List<String> images() {
		return images;
	}

	public OllamaGenerateRequest image(File image) throws IOException {
		if(images == null) images = new ArrayList<>(1);
		images.add(Base64.encode(Files.readAllBytes(image.toPath())));
		return this;
	}

	public OllamaGenerateRequest image(String imagePathOrBase64) {
		if(images == null) images = new ArrayList<>(1);
		images.add(imagePathOrBase64);
		return this;
	}

	public OllamaGenerateRequest images(Collection<String> images) {
		this.images = images == null ? null : new ArrayList<>(images);
		return this;
	}

	public OllamaGenerateRequest images(String... images) {
		this.images = images == null ? null : Arrays.asList(images);
		return this;
	}

	@Override
	public String keepAlive() {
		return keepAlive;
	}

	@Override
	public OllamaGenerateRequest keepAlive(String keepAlive) {
		this.keepAlive = keepAlive;
		return this;
	}

	public Boolean stream() {
		return stream;
	}

	public OllamaGenerateRequest stream(Boolean stream) {
		this.stream = stream;
		return this;
	}

	@Override
	public OllamaGenerateRequest self() {
		return this;
	}
}
