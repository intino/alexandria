package io.intino.alexandria.ollama;

import io.intino.alexandria.Json;
import io.intino.alexandria.ollama.requests.*;
import io.intino.alexandria.ollama.responses.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

public class OllamaHttpClient implements Ollama {

	private static final String DEFAULT_OLLAMA_BASE_URL = "http://localhost:11434";

	private final HttpClient httpClient;
	private final String baseUrl;
	private Duration timeout;
	private final Map<String, String> commonHeaders = new HashMap<>();

	public OllamaHttpClient() {
		this(DEFAULT_OLLAMA_BASE_URL);
	}

	public OllamaHttpClient(String baseUrl) {
		this(baseUrl, Duration.of(5, ChronoUnit.MINUTES), HttpClient.newHttpClient());
	}

	public OllamaHttpClient(String baseUrl, Duration timeout) {
		this(baseUrl, timeout, HttpClient.newHttpClient());
	}

	public OllamaHttpClient(String baseUrl, Duration timeout, HttpClient httpClient) {
		this.baseUrl = requireNonNull(baseUrl);
		this.timeout = timeout;
		this.httpClient = httpClient;
	}

	@Override
	public OllamaChatResponse chat(String model, String message) throws OllamaAPIException {
		return chat(new OllamaChatRequest().model(model).addMessage(OllamaMessage.Role.user, message));
	}

	@Override
	public OllamaChatResponse chat(String model, OllamaMessage... messages) throws OllamaAPIException {
		return chat(new OllamaChatRequest().model(model).messages(messages));
	}

	@Override
	public OllamaChatResponse chat(OllamaChatRequest chatRequest) throws OllamaAPIException {
		OllamaChatResponse response = call(post("/api/chat", chatRequest.stream(false)), OllamaChatResponse.class);
		if(chatRequest.tools() != null && response.message().hasToolCalls()) {
			for(var toolCall : response.message().toolCalls()) {
				var toolDefinition = chatRequest.tools().stream()
						.filter(t -> t.function().name().equals(toolCall.function().name()))
						.findFirst().orElse(null);
				if(toolDefinition == null) continue;
				toolCall.function().binding(toolDefinition.function().binding());
			}
		}
		return response;
	}

	@Override
	public StreamResponse<OllamaChatResponse> chatStream(String model, String message) throws OllamaAPIException {
		return chatStream(new OllamaChatRequest().model(model).addMessage(OllamaMessage.Role.user, message));
	}

	@Override
	public StreamResponse<OllamaChatResponse> chatStream(String model, OllamaMessage... messages) throws OllamaAPIException {
		return chatStream(new OllamaChatRequest().model(model).messages(messages));
	}

	@Override
	public StreamResponse<OllamaChatResponse> chatStream(OllamaChatRequest chatRequest) throws OllamaAPIException {
		return callStream(post("/api/chat", chatRequest.stream(true)), OllamaChatResponse.class);
	}

	@Override
	public OllamaGenerateResponse generate(String model, String prompt) throws OllamaAPIException {
		return generate(new OllamaGenerateRequest().model(model).prompt(prompt));
	}

	@Override
	public OllamaGenerateResponse generate(OllamaGenerateRequest generateRequest) throws OllamaAPIException {
		return call(post("/api/generate", generateRequest.stream(false)), OllamaGenerateResponse.class);
	}

	@Override
	public StreamResponse<OllamaGenerateResponse> generateStream(String model, String prompt) throws OllamaAPIException {
		return generateStream(new OllamaGenerateRequest().model(model).prompt(prompt));
	}

	@Override
	public StreamResponse<OllamaGenerateResponse> generateStream(OllamaGenerateRequest generateRequest) throws OllamaAPIException {
		return callStream(post("/api/generate", generateRequest.stream(true)), OllamaGenerateResponse.class);
	}

	@Override
	public OllamaEmbedResponse.OfDouble embed(OllamaEmbedRequest embedRequest) throws OllamaAPIException {
		return call(post("/api/embed", embedRequest), OllamaEmbedResponse.OfDouble.class);
	}

	@Override
	public OllamaEmbedResponse.OfFloat embedFloats(OllamaEmbedRequest embedRequest) throws OllamaAPIException {
		return call(post("/api/embed", embedRequest), OllamaEmbedResponse.OfFloat.class);
	}

	@Override
	public OllamaCreateModelResponse newClient(String name, String modelFile) throws OllamaAPIException {
		return newClient(new OllamaCreateModelRequest().name(name).modelfile(modelFile));
	}

	@Override
	public OllamaCreateModelResponse newClient(String name, ModelFile modelFile) throws OllamaAPIException {
		return newClient(new OllamaCreateModelRequest().name(name).modelfile(modelFile));
	}

	@Override
	public OllamaCreateModelResponse newClient(OllamaCreateModelRequest createModelRequest) throws OllamaAPIException {
		return call(post("/api/create", createModelRequest), OllamaCreateModelResponse.class);
	}

	@Override
	public StreamResponse<OllamaCreateModelResponse> createStream(OllamaCreateModelRequest createModelRequest) throws OllamaAPIException {
		return callStream(post("/api/create", createModelRequest), OllamaCreateModelResponse.class);
	}

	@Override
	public OllamaListResponse list() throws OllamaAPIException {
		return call(get("/api/tags"), OllamaListResponse.class);
	}

	@Override
	public OllamaShowResponse show(OllamaShowRequest showRequest) throws OllamaAPIException {
		return call(post("/api/show", showRequest), OllamaShowResponse.class);
	}

	@Override
	public OllamaShowResponse show(String name, boolean verbose) throws OllamaAPIException {
		return call(post("/api/show", new OllamaShowRequest().name(name).verbose(verbose)), OllamaShowResponse.class);
	}

	@Override
	public boolean pullIfNotExists(String model) throws OllamaAPIException {
		if(exists(model)) return false;
		pull(model);
		return true;
	}

	@Override
	public OllamaPullResponse pull(String model) throws OllamaAPIException {
		return call(post("/api/pull", new OllamaPullRequest().name(model).stream(false)), OllamaPullResponse.class);
	}

	@Override
	public OllamaPullResponse pull(OllamaPullRequest pullRequest) throws OllamaAPIException {
		return call(post("/api/pull", pullRequest.stream(false)), OllamaPullResponse.class);
	}

	@Override
	public StreamResponse<OllamaPullResponse> pullStream(String model) throws OllamaAPIException {
		return pullStream(new OllamaPullRequest().name(model));
	}

	@Override
	public StreamResponse<OllamaPullResponse> pullStream(OllamaPullRequest pullRequest) throws OllamaAPIException {
		return callStream(post("/api/pull", pullRequest.stream(true)), OllamaPullResponse.class);
	}

	@Override
	public OllamaPushResponse push(OllamaPushRequest pushRequest) throws OllamaAPIException {
		return call(post("/api/push", pushRequest.stream(true)), OllamaPushResponse.class);
	}

	@Override
	public StreamResponse<OllamaPushResponse> pushStream(OllamaPushRequest pushRequest) throws OllamaAPIException {
		return callStream(post("/api/push", pushRequest.stream(true)), OllamaPushResponse.class);
	}

	@Override
	public OllamaPsResponse ps() throws OllamaAPIException {
		return call(get("/api/ps"), OllamaPsResponse.class);
	}

	@Override
	public void copy(OllamaCopyRequest copyRequest) throws OllamaAPIException {
		call(post("/api/copy", copyRequest));
	}

	@Override
	public boolean exists(String model) throws OllamaAPIException {
		return list().modelNames().contains(model.replace(":latest", ""));
	}

	@Override
	public void delete(String name) throws OllamaAPIException {
		call(deleteRequest("/api/delete", Json.toJson(Map.of("name", name))));
	}

	@Override
	public boolean deleteIfExists(String name) throws OllamaAPIException {
		if(exists(name)) {
			call(deleteRequest("/api/delete", Json.toJson(Map.of("name", name))));
			return true;
		}
		return false;
	}

	@Override
	public boolean existsBlob(String digest) throws OllamaAPIException {
		try {
			call(head("/api/blobs/sha256:" + digest));
			return true;
		} catch (OllamaAPIException e) {
			if(e.statusCode() != null && e.statusCode() == 404) return false;
			throw e;
		}
	}

	@Override
	public void createBlob(File file, String digest) throws OllamaAPIException, FileNotFoundException {
		call(post("/api/blobs/sha256:" + digest, file));
	}

	@Override
	public void createBlob(File file) throws OllamaAPIException, FileNotFoundException {
		call(post("/api/blobs/sha256:" + Ollama.sha256(file), file));
	}

	public <T extends OllamaResponse> T call(HttpRequest request, Class<T> responseType) throws OllamaAPIException {
		try {
			HttpResponse<String> httpResponse = sendRequest(request);
			return OllamaResponse.fromJson(httpResponse.body(), responseType);
		} catch (OllamaAPIException e) {
			throw e;
		} catch (Exception e) {
			throw new OllamaAPIException("Error when calling " + request.uri() + ": " + e.getMessage(), e);
		}
	}

	public void call(HttpRequest request) throws OllamaAPIException {
		try {
			sendRequest(request);
		} catch (OllamaAPIException e) {
			throw e;
		} catch (Exception e) {
			throw new OllamaAPIException("Error when calling " + request.uri() + ": " + e.getMessage(), e);
		}
	}

	private HttpResponse<String> sendRequest(HttpRequest request) throws IOException, InterruptedException, OllamaAPIException {
		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString(UTF_8));
		if(response.statusCode() < 200 || response.statusCode() > 299) {
			String msg = response.body();
			if(msg.contains("{\"error\":")) msg = Json.fromJson(msg, Map.class).get("error").toString();
			throw new OllamaAPIException(response.statusCode(), "Error (" + response.statusCode() + ") when calling " + request.uri() + ": " + msg);
		}
		return response;
	}

	public <T extends OllamaResponse> StreamResponse<T> callStream(HttpRequest request, Class<T> responseType) throws OllamaAPIException {
		try {
			HttpResponse<InputStream> response = httpClient.send(request, BodyHandlers.ofInputStream());
			if(response.statusCode() < 200 || response.statusCode() > 299) {
				try(InputStream body = response.body()) {
					String msg = body != null ? new String(body.readAllBytes(), UTF_8) : "";
					if(msg.contains("{\"error\":")) msg = Json.fromJson(msg, Map.class).get("error").toString();
					throw new OllamaAPIException(response.statusCode(), "Error (" + response.statusCode() + ") when calling " + request.uri() + ": " + msg);
				}
			}
			return new StreamResponse<>(response.body(), responseType);
		} catch (OllamaAPIException e) {
			throw e;
		} catch (Exception e) {
			throw new OllamaAPIException("Error when calling " + request.uri() + ": " + e.getMessage(), e);
		}
	}

	private HttpRequest post(String endpoint, OllamaRequest ollamaRequest) {
		return request(endpoint)
				.header("Content-Type", "application/json")
				.timeout(timeout)
				.POST(HttpRequest.BodyPublishers.ofString(ollamaRequest.toJson(), UTF_8))
				.build();
	}

	private HttpRequest post(String endpoint, File file) throws FileNotFoundException {
		return request(endpoint)
				.header("Content-Type", "application/octet-stream")
				.timeout(timeout)
				.POST(HttpRequest.BodyPublishers.ofFile(file.toPath()))
				.build();
	}

	private HttpRequest post(String endpoint) {
		return request(endpoint)
				.header("Content-Type", "application/json")
				.timeout(timeout)
				.POST(HttpRequest.BodyPublishers.noBody())
				.build();
	}

	private HttpRequest get(String endpoint) {
		return request(endpoint)
				.timeout(timeout)
				.GET()
				.build();
	}

	private HttpRequest head(String endpoint) {
		return request(endpoint)
				.timeout(timeout)
				.HEAD()
				.build();
	}

	private HttpRequest deleteRequest(String endpoint, String body) {
		return request(endpoint)
				.timeout(timeout)
				.method("DELETE", HttpRequest.BodyPublishers.ofString(body, UTF_8))
				.build();
	}

	private HttpRequest.Builder request(String endpoint) {
		HttpRequest.Builder builder = HttpRequest.newBuilder().uri(URI.create(baseUrl + endpoint));
		for(var entry : commonHeaders.entrySet()) {
			builder.setHeader(entry.getKey(), entry.getValue());
		}
		return builder;
	}

	@Override
	public String baseUrl() {
		return baseUrl;
	}

	@Override
	public Duration timeout() {
		return timeout;
	}

	@Override
	public OllamaHttpClient timeout(Duration timeout) {
		this.timeout = timeout;
		return this;
	}

	@Override
	public Map<String, String> commonHeaders() {
		return Collections.unmodifiableMap(commonHeaders);
	}

	@Override
	public OllamaHttpClient setCommonHeaders(Map<String, String> headers) {
		this.commonHeaders.clear();
		this.commonHeaders.putAll(headers);
		return this;
	}

	@Override
	public String getCommonHeader(String name) {
		return commonHeaders.get(name);
	}

	@Override
	public OllamaHttpClient setCommonHeader(String name, String value) {
		commonHeaders.put(name, value);
		return this;
	}

	@Override
	public OllamaHttpClient removeCommonHeader(String name) {
		commonHeaders.remove(name);
		return this;
	}

	@Override
	public void close() {
		httpClient.close();
	}

}
