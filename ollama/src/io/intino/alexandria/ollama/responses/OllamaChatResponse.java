package io.intino.alexandria.ollama.responses;

import com.google.gson.annotations.SerializedName;
import io.intino.alexandria.ollama.OllamaMessage;

import java.time.OffsetDateTime;

public class OllamaChatResponse extends OllamaResponse {

	private String model;
	@SerializedName("created_at")
	private String createdAt;
	private OllamaMessage message;
	private boolean done;
	@SerializedName("done_reason")
	private String doneReason;
	@SerializedName("total_duration")
	private long totalDuration;
	@SerializedName("load_duration")
	private long loadDuration;
	@SerializedName("prompt_eval_count")
	private long promptEvalCount;
	@SerializedName("prompt_eval_duration")
	private long promptEvalDuration;
	@SerializedName("eval_count")
	private long evalCount;
	@SerializedName("eval_duration")
	private long evalDuration;

	public String model() {
		return model;
	}

	public OllamaChatResponse model(String model) {
		this.model = model;
		return this;
	}

	public String createdAtStr() {
		return createdAt;
	}

	public OffsetDateTime createdAt() {
		return OffsetDateTime.parse(createdAt);
	}

	public OllamaChatResponse createdAt(String createdAt) {
		this.createdAt = createdAt;
		return this;
	}

	public String text() {
		return message == null || message.content() == null ? null : message.content();
	}

	public OllamaMessage message() {
		return message;
	}

	public OllamaChatResponse message(OllamaMessage message) {
		this.message = message;
		return this;
	}

	public boolean done() {
		return done;
	}

	public OllamaChatResponse done(boolean done) {
		this.done = done;
		return this;
	}

	public String doneReason() {
		return doneReason;
	}

	public OllamaChatResponse doneReason(String doneReason) {
		this.doneReason = doneReason;
		return this;
	}

	public long totalDuration() {
		return totalDuration;
	}

	public OllamaChatResponse totalDuration(long totalDuration) {
		this.totalDuration = totalDuration;
		return this;
	}

	public long loadDuration() {
		return loadDuration;
	}

	public OllamaChatResponse loadDuration(long loadDuration) {
		this.loadDuration = loadDuration;
		return this;
	}

	public long promptEvalCount() {
		return promptEvalCount;
	}

	public OllamaChatResponse promptEvalCount(long promptEvalCount) {
		this.promptEvalCount = promptEvalCount;
		return this;
	}

	public long promptEvalDuration() {
		return promptEvalDuration;
	}

	public OllamaChatResponse promptEvalDuration(long promptEvalDuration) {
		this.promptEvalDuration = promptEvalDuration;
		return this;
	}

	public long evalCount() {
		return evalCount;
	}

	public OllamaChatResponse evalCount(long evalCount) {
		this.evalCount = evalCount;
		return this;
	}

	public long evalDuration() {
		return evalDuration;
	}

	public OllamaChatResponse evalDuration(long evalDuration) {
		this.evalDuration = evalDuration;
		return this;
	}

	public float tokensPerSecond() {
		return evalCount / (float) evalDuration * 1_000_000_000;
	}
}
