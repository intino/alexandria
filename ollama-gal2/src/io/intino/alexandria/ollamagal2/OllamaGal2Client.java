package io.intino.alexandria.ollamagal2;

import es.monentia.gal2.api.box.Gal2ModelsAccessor;
import es.monentia.gal2.api.box.schemas.ModelDownloadRequest;
import es.monentia.gal2.api.box.schemas.ModelExecutionRequest;
import es.monentia.gal2.api.box.schemas.ModelExecutionResponse;
import io.intino.alexandria.Json;
import io.intino.alexandria.Resource;
import io.intino.alexandria.ollama.Ollama;
import io.intino.alexandria.ollama.OllamaAPIException;
import io.intino.alexandria.ollama.OllamaMessage;
import io.intino.alexandria.ollama.requests.*;
import io.intino.alexandria.ollama.responses.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class OllamaGal2Client implements Ollama {

	private final String baseUrl;
	private Duration timeout;
	private final String gal2ModelName;
	private final String gal2Namespace;
	private final String authToken;
	private Gal2ModelsAccessor gal2;

	public OllamaGal2Client(String baseUrl, String gal2ModelName, String gal2Namespace, String authToken) {
		this(baseUrl, Duration.of(10, ChronoUnit.MINUTES), gal2ModelName, gal2Namespace, authToken);
	}

	public OllamaGal2Client(String baseUrl, Duration timeout, String gal2ModelName, String gal2Namespace, String authToken) {
		this.baseUrl = baseUrl;
		this.timeout = timeout;
		this.gal2ModelName = gal2ModelName;
		this.gal2Namespace = gal2Namespace;
		this.authToken = authToken;
		try {
			this.gal2 = new Gal2ModelsAccessor(URI.create(baseUrl).toURL(), (int) timeout.toMillis());
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public OllamaChatResponse chat(OllamaChatRequest chatRequest) throws OllamaAPIException {
		return post("/api/chat", chatRequest.stream(false), OllamaChatResponse.class);
	}

	@Override
	public StreamResponse<OllamaChatResponse> chatStream(String model, String message) throws OllamaAPIException {
		return chatStream(new OllamaChatRequest().model(model).addMessage(OllamaMessage.Role.user, message));
	}

	@Override
	public StreamResponse<OllamaChatResponse> chatStream(OllamaChatRequest chatRequest) throws OllamaAPIException {
		return postStream("/api/chat", chatRequest.stream(true), OllamaChatResponse.class);
	}

	@Override
	public OllamaGenerateResponse generate(OllamaGenerateRequest generateRequest) throws OllamaAPIException {
		return post("/api/generate", generateRequest.stream(false), OllamaGenerateResponse.class);
	}

	@Override
	public StreamResponse<OllamaGenerateResponse> generateStream(OllamaGenerateRequest generateRequest) throws OllamaAPIException {
		return postStream("/api/generate", generateRequest.stream(true), OllamaGenerateResponse.class);
	}

	@Override
	public OllamaEmbedResponse.OfDouble embed(OllamaEmbedRequest embedRequest) throws OllamaAPIException {
		return post("/api/embed", embedRequest, OllamaEmbedResponse.OfDouble.class);
	}

	@Override
	public OllamaEmbedResponse.OfFloat embedFloats(OllamaEmbedRequest embedRequest) throws OllamaAPIException {
		return post("/api/embed", embedRequest, OllamaEmbedResponse.OfFloat.class);
	}

	@Override
	public OllamaCreateModelResponse createModel(OllamaCreateModelRequest createRequest) throws OllamaAPIException {
		return post("/api/create", createRequest.stream(false), OllamaCreateModelResponse.class);
	}

	@Override
	public StreamResponse<OllamaCreateModelResponse> createModelStream(OllamaCreateModelRequest createRequest) throws OllamaAPIException {
		return postStream("/api/create", createRequest.stream(true), OllamaCreateModelResponse.class);
	}

	@Override
	public OllamaListResponse list() throws OllamaAPIException {
		return get("/api/tags", OllamaListResponse.class);
	}

	@Override
	public OllamaShowResponse show(OllamaShowRequest showRequest) throws OllamaAPIException {
		return post("/api/show", showRequest, OllamaShowResponse.class);
	}

	@Override
	public OllamaShowResponse show(String model, boolean verbose) throws OllamaAPIException {
		return show(new OllamaShowRequest().name(model).verbose(verbose));
	}

	@Override
	public OllamaPullResponse pull(OllamaPullRequest pullRequest) throws OllamaAPIException {
		return post("/api/pull", pullRequest.stream(false), OllamaPullResponse.class);
	}

	@Override
	public StreamResponse<OllamaPullResponse> pullStream(OllamaPullRequest pullRequest) throws OllamaAPIException {
		return postStream("/api/pull", pullRequest.stream(true), OllamaPullResponse.class);
	}

	@Override
	public OllamaPushResponse push(OllamaPushRequest pushRequest) throws OllamaAPIException {
		return post("/api/push", pushRequest.stream(false), OllamaPushResponse.class);
	}

	@Override
	public StreamResponse<OllamaPushResponse> pushStream(OllamaPushRequest pushRequest) throws OllamaAPIException {
		return postStream("/api/push", pushRequest.stream(true), OllamaPushResponse.class);
	}

	@Override
	public OllamaPsResponse ps() throws OllamaAPIException {
		return get("/api/ps", OllamaPsResponse.class);
	}

	@Override
	public void copy(OllamaCopyRequest copyRequest) throws OllamaAPIException {
		post("/api/copy", copyRequest);
	}

	@Override
	public void delete(String name) throws OllamaAPIException {
		deleteRequest("/api/delete", Json.toJson(Map.of("name", name)));
	}

	@Override
	public boolean existsBlob(String digest) throws OllamaAPIException {
		try {
			head("/api/blobs/sha256:" + digest);
			return true;
		} catch (OllamaAPIException e) {
			if(e.statusCode() != null && e.statusCode() == 404) return false;
			throw e;
		}
	}

	@Override
	public void createBlob(File file, String digest) throws OllamaAPIException {
		post("/api/blobs/sha256:" + digest, file);
	}

	@Override
	public void createBlob(File file) throws OllamaAPIException {
		post("/api/blobs/sha256:" + Ollama.sha256(file), file);
	}

	private ModelExecutionResponse head(String endpoint) throws OllamaAPIException {
		try {
			return gal2.postExecuteModel(authToken, new ModelExecutionRequest()
							.headers(Map.of("Content-Type", "application/json"))
							.namespace(gal2Namespace)
							.name(gal2ModelName)
							.endpoint(endpoint)
							.method(ModelExecutionRequest.Method.HEAD)
							.timeoutSeconds(timeout.toSeconds())
					, Collections.emptyList());
		} catch (Throwable e) {
			throw new OllamaAPIException(e);
		}
	}

	private ModelExecutionResponse deleteRequest(String endpoint, String body) throws OllamaAPIException {
		try {
			return gal2.postExecuteModel(authToken, new ModelExecutionRequest()
							.headers(Map.of("Content-Type", "application/json"))
							.namespace(gal2Namespace)
							.name(gal2ModelName)
							.endpoint(endpoint)
							.method(ModelExecutionRequest.Method.DELETE)
							.input(List.of(body))
							.timeoutSeconds(timeout.toSeconds())
					, Collections.emptyList());
		} catch (Throwable e) {
			throw new OllamaAPIException(e);
		}
	}

	private ModelExecutionResponse post(String endpoint, File file) throws OllamaAPIException {
		try {
			return gal2.postExecuteModel(authToken, new ModelExecutionRequest()
							.headers(Map.of("Content-Type", "application/json"))
							.namespace(gal2Namespace)
							.name(gal2ModelName)
							.endpoint(endpoint)
							.method(ModelExecutionRequest.Method.POST)
							.timeoutSeconds(timeout.toSeconds())
					, List.of(new Resource(file)));
		} catch (Throwable e) {
			throw new OllamaAPIException(e);
		}
	}

	private ModelExecutionResponse post(String endpoint, OllamaRequest request) throws OllamaAPIException {
		try {
			return gal2.postExecuteModel(authToken, new ModelExecutionRequest()
							.headers(Map.of("Content-Type", "application/json"))
							.namespace(gal2Namespace)
							.name(gal2ModelName)
							.endpoint(endpoint)
							.method(ModelExecutionRequest.Method.POST)
							.input(List.of(request.toJson()))
							.timeoutSeconds(timeout.toSeconds())
					, Collections.emptyList());
		} catch (Throwable e) {
			throw new OllamaAPIException(e);
		}
	}

	private <T extends OllamaResponse> T post(String endpoint, OllamaRequest request, Class<T> responseType) throws OllamaAPIException {
		try {
			var response = post(endpoint, request);
			return OllamaResponse.fromJson(response.output(), responseType);
		} catch (Throwable e) {
			throw new OllamaAPIException(e);
		}
	}

	private <T extends OllamaResponse> StreamResponse<T> postStream(String endpoint, OllamaRequest request, Class<T> responseType) throws OllamaAPIException {
		try {
			var response = gal2.postDownloadFromModel(authToken, (ModelDownloadRequest) new ModelDownloadRequest()
							.headers(Map.of("Content-Type", "application/json"))
							.namespace(gal2Namespace)
							.name(gal2ModelName)
							.endpoint(endpoint)
							.method(ModelExecutionRequest.Method.POST)
							.input(List.of(request.toJson()))
							.timeoutSeconds(timeout.toSeconds())
					, Collections.emptyList());
			return new StreamResponse<>(response.inputStream(), responseType);
		} catch (Throwable e) {
			throw new OllamaAPIException(e);
		}
	}

	private <T extends OllamaResponse> T get(String endpoint, Class<T> responseType) throws OllamaAPIException {
		try {
			var response = gal2.postExecuteModel(authToken, new ModelExecutionRequest()
							.namespace(gal2Namespace)
							.name(gal2ModelName)
							.endpoint(endpoint)
							.method(ModelExecutionRequest.Method.GET)
							.timeoutSeconds(timeout.toSeconds())
					, Collections.emptyList());
			return OllamaResponse.fromJson(response.output(), responseType);
		} catch (Throwable e) {
			throw new OllamaAPIException(e);
		}
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
	public Ollama timeout(Duration timeout) {
		try {
			this.gal2 = new Gal2ModelsAccessor(URI.create(baseUrl).toURL(), timeout == null ? 0 : (int) timeout.toMillis());
			this.timeout = timeout;
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	@Override
	public Ollama setCommonHeader(String name, String value) {
		gal2.addCommonHeader(name, value);
		return this;
	}

	@Override
	public void close() {
	}
}
