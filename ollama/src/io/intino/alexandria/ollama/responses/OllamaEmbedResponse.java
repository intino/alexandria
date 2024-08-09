package io.intino.alexandria.ollama.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class OllamaEmbedResponse<T> extends OllamaResponse implements Iterable<T> {

	private String model;
	@SerializedName("total_duration")
	private long totalDuration;
	@SerializedName("load_duration")
	private long loadDuration;
	@SerializedName("prompt_eval_count")
	private long promptEvalCount;

	public String model() {
		return model;
	}

	public OllamaEmbedResponse<T> model(String model) {
		this.model = model;
		return this;
	}

	public long totalDuration() {
		return totalDuration;
	}

	public OllamaEmbedResponse<T> totalDuration(long totalDuration) {
		this.totalDuration = totalDuration;
		return this;
	}

	public long loadDuration() {
		return loadDuration;
	}

	public OllamaEmbedResponse<T> loadDuration(long loadDuration) {
		this.loadDuration = loadDuration;
		return this;
	}

	public long promptEvalCount() {
		return promptEvalCount;
	}

	public OllamaEmbedResponse<T> promptEvalCount(long promptEvalCount) {
		this.promptEvalCount = promptEvalCount;
		return this;
	}

	public abstract T[] embeddings();

	public abstract OllamaEmbedResponse<T> embeddings(T[] embeddings);


	public static class OfDouble extends OllamaEmbedResponse<double[]> {

		private double[][] embeddings = new double[0][0];

		@Override
		public OllamaEmbedResponse.OfDouble model(String model) {
			super.model(model);
			return this;
		}

		@Override
		public double[][] embeddings() {
			return embeddings;
		}

		public List<List<Double>> embeddingsAsLists() {
			List<List<Double>> list = new ArrayList<>(embeddings.length);
			for(double[] vector : embeddings) {
				list.add(Arrays.stream(vector).boxed().collect(Collectors.toList()));
			}
			return list;
		}

		@Override
		public OllamaEmbedResponse.OfDouble embeddings(double[][] embeddings) {
			this.embeddings = embeddings;
			return this;
		}

		@Override
		public Iterator<double[]> iterator() {
			return Arrays.stream(embeddings).iterator();
		}
	}

	public static class OfFloat extends OllamaEmbedResponse<float[]> {

		private float[][] embeddings = new float[0][0];

		@Override
		public OllamaEmbedResponse.OfFloat model(String model) {
			super.model(model);
			return this;
		}

		@Override
		public float[][] embeddings() {
			return embeddings;
		}

		public List<List<Float>> embeddingsAsList() {
			List<List<Float>> list = new ArrayList<>(embeddings.length);
			for(float[] vector : embeddings) {
				List<Float> boxed = new ArrayList<>(vector.length);
				for(float v : vector) boxed.add(v);
				list.add(boxed);
			}
			return list;
		}

		@Override
		public OllamaEmbedResponse.OfFloat embeddings(float[][] embeddings) {
			this.embeddings = embeddings;
			return this;
		}

		@Override
		public Iterator<float[]> iterator() {
			return Arrays.stream(embeddings).iterator();
		}
	}
}
