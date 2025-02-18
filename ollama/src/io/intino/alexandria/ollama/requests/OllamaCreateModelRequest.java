package io.intino.alexandria.ollama.requests;

import io.intino.alexandria.ollama.OllamaMessage;
import io.intino.alexandria.ollama.OllamaParameters;

import java.util.*;

public class OllamaCreateModelRequest extends OllamaRequest implements OllamaParameters<OllamaCreateModelRequest> {

	private String model;
	private String quantize;
	private String from;
	private Map<String, String> files;
	private Map<String, Object> parameters = new LinkedHashMap<>();
	private String template;
	private String system;
	private Map<String, String> adapters;
	private List<OllamaMessage> messages = new ArrayList<>();
	private String license;
	private boolean stream;

	public String model() {
		return model;
	}

	public OllamaCreateModelRequest model(String model) {
		this.model = model;
		return this;
	}

	public String quantize() {
		return quantize;
	}

	public OllamaCreateModelRequest quantize(String quantize) {
		this.quantize = quantize;
		return this;
	}

	public String from() {
		return from;
	}

	public OllamaCreateModelRequest from(String modelOrPathToBinFile) {
		this.from = modelOrPathToBinFile;
		return this;
	}

	public Map<String, String> files() {
		return files;
	}

	public OllamaCreateModelRequest files(Map<String, String> files) {
		this.files = files;
		return this;
	}

	@Override
	public Map<String, Object> parametersMap() {
		return parameters;
	}

	public Map<String, Object> parameters() {
		return parametersMap();
	}

	public OllamaCreateModelRequest parameters(Map<String, Object> parameters) {
		this.parameters = parameters == null ? new HashMap<>(0) : new HashMap<>(parameters);
		return this;
	}

	public String template() {
		return template;
	}

	public OllamaCreateModelRequest template(String template) {
		this.template = template;
		return this;
	}

	public String system() {
		return system;
	}

	public OllamaCreateModelRequest system(String system) {
		this.system = system;
		return this;
	}

	public Map<String, String> adapters() {
		return adapters;
	}

	public OllamaCreateModelRequest addAdapter(String pathToLoRAGGMLFile, String sha256) {
		if(adapters == null) adapters = new LinkedHashMap<>();
		this.adapters.put(pathToLoRAGGMLFile, sha256);
		return this;
	}

	public OllamaCreateModelRequest adapters(Map<String, String> adapters) {
		this.adapters = adapters == null ? null : new LinkedHashMap<>(adapters);
		return this;
	}

	public List<OllamaMessage> messages() {
		return messages;
	}

	public OllamaCreateModelRequest addMessage(OllamaMessage.Role role, String content) {
		return addMessage(new OllamaMessage(role, content));
	}

	public OllamaCreateModelRequest addMessage(OllamaMessage message) {
		if(messages == null) messages = new ArrayList<>(1);
		messages.add(message);
		return this;
	}

	public OllamaCreateModelRequest messages(Collection<OllamaMessage> messages) {
		this.messages = messages == null ? null : new ArrayList<>(messages);
		return this;
	}

	public OllamaCreateModelRequest messages(OllamaMessage... messages) {
		this.messages = messages == null ? null : Arrays.asList(messages);
		return this;
	}

	public String license() {
		return license;
	}

	public OllamaCreateModelRequest license(String license) {
		this.license = license;
		return this;
	}

	public boolean stream() {
		return stream;
	}

	public OllamaCreateModelRequest stream(boolean stream) {
		this.stream = stream;
		return this;
	}
}
