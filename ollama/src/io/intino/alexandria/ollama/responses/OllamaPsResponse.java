package io.intino.alexandria.ollama.responses;

import com.google.gson.annotations.SerializedName;
import io.intino.alexandria.Json;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OllamaPsResponse extends OllamaResponse {

	private List<Model> models = new ArrayList<>();

	public OllamaPsResponse() {
	}

	public OllamaPsResponse(List<Model> models) {
		this.models = models;
	}

	public List<Model> models() {
		return models;
	}

	public OllamaPsResponse models(List<Model> models) {
		this.models = models;
		return this;
	}

	public static class Model {

		private String name;
		private String model;
		private long size;
		private String digest;
		private Details details;
		@SerializedName("expires_at")
		private String expiresAt;
		@SerializedName("size_vram")
		private long sizeVRAM;

		public String name() {
			return name;
		}

		public Model name(String name) {
			this.name = name;
			return this;
		}

		public String model() {
			return model;
		}

		public Model model(String model) {
			this.model = model;
			return this;
		}

		public long size() {
			return size;
		}

		public Model size(long size) {
			this.size = size;
			return this;
		}

		public String digest() {
			return digest;
		}

		public Model digest(String digest) {
			this.digest = digest;
			return this;
		}

		public Details details() {
			return details;
		}

		public Model details(Details details) {
			this.details = details;
			return this;
		}

		public String expiresAtStr() {
			return expiresAt;
		}

		public OffsetDateTime expiresAt() {
			return OffsetDateTime.parse(expiresAt);
		}

		public Model expiresAt(String expiresAt) {
			this.expiresAt = expiresAt;
			return this;
		}

		public long sizeVRAM() {
			return sizeVRAM;
		}

		public Model sizeVRAM(long sizeVRAM) {
			this.sizeVRAM = sizeVRAM;
			return this;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Model model1 = (Model) o;
			return size == model1.size && sizeVRAM == model1.sizeVRAM && Objects.equals(name, model1.name) && Objects.equals(model, model1.model) && Objects.equals(digest, model1.digest) && Objects.equals(details, model1.details) && Objects.equals(expiresAt, model1.expiresAt);
		}

		@Override
		public int hashCode() {
			return Objects.hash(name, model, size, digest, details, expiresAt, sizeVRAM);
		}

		@Override
		public String toString() {
			return Json.toJson(this);
		}

		public static class Details {

			@SerializedName("parent_model")
			private String parentModel;
			private String format;
			private String family;
			private String[] families;
			@SerializedName("parameter_size")
			private String parameterSize;
			@SerializedName("quantization_level")
			private String quantizationLevel;

			public String parentModel() {
				return parentModel;
			}

			public Details parentModel(String parentModel) {
				this.parentModel = parentModel;
				return this;
			}

			public String format() {
				return format;
			}

			public Details format(String format) {
				this.format = format;
				return this;
			}

			public String family() {
				return family;
			}

			public Details family(String family) {
				this.family = family;
				return this;
			}

			public String[] families() {
				return families;
			}

			public Details families(String[] families) {
				this.families = families;
				return this;
			}

			public String parameterSize() {
				return parameterSize;
			}

			public Details parameterSize(String parameterSize) {
				this.parameterSize = parameterSize;
				return this;
			}

			public String quantizationLevel() {
				return quantizationLevel;
			}

			public Details quantizationLevel(String quantizationLevel) {
				this.quantizationLevel = quantizationLevel;
				return this;
			}

			@Override
			public boolean equals(Object o) {
				if (this == o) return true;
				if (o == null || getClass() != o.getClass()) return false;
				Details details = (Details) o;
				return Objects.equals(parentModel, details.parentModel) && Objects.equals(format, details.format) && Objects.equals(family, details.family) && Objects.deepEquals(families, details.families) && Objects.equals(parameterSize, details.parameterSize) && Objects.equals(quantizationLevel, details.quantizationLevel);
			}

			@Override
			public int hashCode() {
				return Objects.hash(parentModel, format, family, Arrays.hashCode(families), parameterSize, quantizationLevel);
			}

			@Override
			public String toString() {
				return Json.toJson(this);
			}
		}
	}
}
