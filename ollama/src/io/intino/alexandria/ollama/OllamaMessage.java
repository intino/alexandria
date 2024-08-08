package io.intino.alexandria.ollama;

import com.google.gson.annotations.SerializedName;
import io.intino.alexandria.Base64;
import io.intino.alexandria.Json;
import io.intino.alexandria.ollama.tools.OllamaToolCall;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class OllamaMessage {

	public enum Role {
		system, user, assistant
	}

	private Role role = Role.user;
	private String content;
	private List<String> images;
	@SerializedName("tool_calls")
	private List<OllamaToolCall> toolCalls;

	public OllamaMessage() {
	}

	public OllamaMessage(String content) {
		this(Role.user, content);
	}

	public OllamaMessage(Role role, String content) {
		this.role = role;
		this.content = content;
	}

	public OllamaMessage(Role role, String content, Collection<String> images) {
		this.role = role;
		this.content = content;
		this.images(images);
	}

	public OllamaMessage(Role role, String content, Collection<String> images, Collection<OllamaToolCall> toolCalls) {
		this.role = role;
		this.content = content;
		this.images(images);
		this.toolCalls(toolCalls);
	}

	public Role role() {
		return role;
	}

	public OllamaMessage role(Role role) {
		this.role = role;
		return this;
	}

	public String content() {
		return content;
	}

	public OllamaMessage content(String content) {
		this.content = content;
		return this;
	}

	public List<String> images() {
		return images;
	}

	public OllamaMessage image(File image) throws IOException {
		if(images == null) images = new ArrayList<>(1);
		images.add(Base64.encode(Files.readAllBytes(image.toPath())));
		return this;
	}

	public OllamaMessage image(String imagePathOrBase64) {
		if(images == null) images = new ArrayList<>(1);
		images.add(imagePathOrBase64);
		return this;
	}

	public OllamaMessage images(Collection<String> images) {
		this.images = images == null ? null : new ArrayList<>(images);
		return this;
	}

	public OllamaMessage images(String... images) {
		this.images = Arrays.asList(images);
		return this;
	}

	public boolean hasToolCalls() {
		return toolCalls != null && !toolCalls.isEmpty();
	}

	public List<OllamaToolCall> toolCalls() {
		return toolCalls;
	}

	public OllamaMessage toolCalls(Collection<OllamaToolCall> toolCalls) {
		this.toolCalls = toolCalls == null ? null : new ArrayList<>(toolCalls);
		return this;
	}

	public OllamaMessage toolCalls(OllamaToolCall... toolCalls) {
		this.toolCalls = toolCalls == null ? null : Arrays.asList(toolCalls);
		return this;
	}

	@Override
	public String toString() {
		return Json.toJson(this);
	}
}
