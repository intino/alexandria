package io.intino.alexandria.ollama.responses;

import com.google.gson.annotations.SerializedName;

public class OllamaGenerateResponse extends OllamaResponse {

	private String model;
	@SerializedName("created_at")
	private String createdAt;
	private String response;
	private boolean done;
	private int[] context;
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

	public OllamaGenerateResponse model(String model) {
		this.model = model;
		return this;
	}

	public String createdAt() {
		return createdAt;
	}

	public OllamaGenerateResponse createdAt(String createdAt) {
		this.createdAt = createdAt;
		return this;
	}

	public String response() {
		return response;
	}

	public String text() {
		return response();
	}

	public OllamaGenerateResponse response(String response) {
		this.response = response;
		return this;
	}

	public boolean done() {
		return done;
	}

	public OllamaGenerateResponse done(boolean done) {
		this.done = done;
		return this;
	}

	public int[] context() {
		return context;
	}

	public OllamaGenerateResponse context(int[] context) {
		this.context = context;
		return this;
	}

	public long totalDuration() {
		return totalDuration;
	}

	public OllamaGenerateResponse totalDuration(long totalDuration) {
		this.totalDuration = totalDuration;
		return this;
	}

	public long loadDuration() {
		return loadDuration;
	}

	public OllamaGenerateResponse loadDuration(long loadDuration) {
		this.loadDuration = loadDuration;
		return this;
	}

	public long promptEvalCount() {
		return promptEvalCount;
	}

	public OllamaGenerateResponse promptEvalCount(long promptEvalCount) {
		this.promptEvalCount = promptEvalCount;
		return this;
	}

	public long promptEvalDuration() {
		return promptEvalDuration;
	}

	public OllamaGenerateResponse promptEvalDuration(long promptEvalDuration) {
		this.promptEvalDuration = promptEvalDuration;
		return this;
	}

	public long evalCount() {
		return evalCount;
	}

	public OllamaGenerateResponse evalCount(long evalCount) {
		this.evalCount = evalCount;
		return this;
	}

	public long evalDuration() {
		return evalDuration;
	}

	public OllamaGenerateResponse evalDuration(long evalDuration) {
		this.evalDuration = evalDuration;
		return this;
	}

	public float tokensPerSecond() {
		return evalCount / (float)evalDuration * 1_000_000_000;
	}
}
