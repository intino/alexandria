package io.intino.alexandria.ollama;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModelFile implements OllamaParameters<ModelFile> {

	private String from;
	private Map<String, Object> parameters = new HashMap<>();
	private String template;
	private String system;
	private List<String> adapters = new ArrayList<>(0);
	private List<OllamaMessage> messages = new ArrayList<>();
	private String license;

	public ModelFile() {
	}

	public ModelFile(String from) {
		this.from = from;
	}

	public String from() {
		return from;
	}

	public ModelFile from(String modelOrPathToBinFile) {
		this.from = modelOrPathToBinFile;
		return this;
	}

	@Override
	public Map<String, Object> parametersMap() {
		return parameters;
	}

	public Map<String, Object> parameters() {
		return parametersMap();
	}

	public ModelFile parameters(Map<String, Object> parameters) {
		this.parameters = parameters == null ? new HashMap<>(0) : new HashMap<>(parameters);
		return this;
	}

	public String template() {
		return template;
	}

	public ModelFile template(String template) {
		this.template = template;
		return this;
	}

	public String system() {
		return system;
	}

	public ModelFile system(String system) {
		this.system = system;
		return this;
	}

	public List<String> adapters() {
		return adapters;
	}

	public ModelFile addAdapter(String pathToLoRAGGMLFile) {
		if(adapters == null) adapters = new ArrayList<>(1);
		this.adapters.add(pathToLoRAGGMLFile);
		return this;
	}

	public ModelFile adapters(Collection<String> adapters) {
		this.adapters = adapters == null ? null : new ArrayList<>(adapters);
		return this;
	}

	public ModelFile adapters(String... adapters) {
		this.adapters = adapters == null ? null : Arrays.asList(adapters);
		return this;
	}

	public List<OllamaMessage> messages() {
		return messages;
	}

	public ModelFile addMessage(OllamaMessage.Role role, String content) {
		return addMessage(new OllamaMessage(role, content));
	}

	public ModelFile addMessage(OllamaMessage message) {
		if(messages == null) messages = new ArrayList<>(1);
		messages.add(message);
		return this;
	}

	public ModelFile messages(Collection<OllamaMessage> messages) {
		this.messages = messages == null ? null : new ArrayList<>(messages);
		return this;
	}

	public ModelFile messages(OllamaMessage... messages) {
		this.messages = messages == null ? null : Arrays.asList(messages);
		return this;
	}

	public String license() {
		return license;
	}

	public ModelFile license(String license) {
		this.license = license;
		return this;
	}

	@Override
	public String toString() {
		return Stream.of(
						renderLicense(),
						"FROM " + from,
						renderAdapters(),
						renderParameters(),
						renderTemplate(),
						renderSystem(),
						renderMessages())
				.filter(s -> !s.isBlank())
				.collect(Collectors.joining("\n"));
	}

	private String renderLicense() {
		return license == null ? "" : "LICENSE \"\"\"\n" + license + "\n\"\"\"";
	}

	private String renderAdapters() {
		return adapters == null ? "" : adapters.stream().map(a -> "ADAPTER " + a).collect(Collectors.joining("\n"));
	}

	private String renderParameters() {
		return parameters == null ? "" : parameters.entrySet().stream()
				.map(e -> "PARAMETER " + e.getKey() + " " + e.getValue())
				.collect(Collectors.joining("\n"));
	}

	private String renderTemplate() {
		return template == null ? "" : "TEMPLATE \"\"\"" + template + "\"\"\"";
	}

	private String renderSystem() {
		return system == null ? "" : "SYSTEM \"\"\"" + system + "\"\"\"";
	}

	private String renderMessages() {
		return messages == null ? "" : messages.stream().map(m -> "MESSAGE " + m.role() + " " + m.content()).collect(Collectors.joining("\n"));
	}
}
