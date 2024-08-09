package io.intino.alexandria.ollama.responses;

import com.google.gson.annotations.SerializedName;
import io.intino.alexandria.Json;

import java.util.Map;

public class OllamaShowResponse extends OllamaResponse {

	private String modelfile;
	private String parameters;
	private String template;
	private Details details;
	@SerializedName("model_info")
	private Map<String, Object> modelInfo;

	public String modelfile() {
		return modelfile;
	}

	public OllamaShowResponse modelfile(String modelfile) {
		this.modelfile = modelfile;
		return this;
	}

	public String parameters() {
		return parameters;
	}

	public OllamaShowResponse parameters(String parameters) {
		this.parameters = parameters;
		return this;
	}

	public String template() {
		return template;
	}

	public OllamaShowResponse template(String template) {
		this.template = template;
		return this;
	}

	public Details details() {
		return details;
	}

	public OllamaShowResponse details(Details details) {
		this.details = details;
		return this;
	}

	public Map<String, Object> modelInfo() {
		return modelInfo;
	}

	public OllamaShowResponse modelInfo(Map<String, Object> modelInfo) {
		this.modelInfo = modelInfo;
		return this;
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
		public String toString() {
			return Json.toJson(this);
		}
	}
}
