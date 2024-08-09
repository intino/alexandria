package io.intino.alexandria.ollama;

import io.intino.alexandria.Json;
import io.intino.alexandria.ollama.requests.*;
import io.intino.alexandria.ollama.responses.*;

import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.time.Duration;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Ollama extends AutoCloseable {

	static Ollama newClient() {
		return new OllamaHttpClient();
	}

	static Ollama newClient(String baseUrl) {
		return new OllamaHttpClient(baseUrl);
	}

	static Ollama newClient(String baseUrl, Duration timeout) {
		return new OllamaHttpClient(baseUrl, timeout);
	}

	default OllamaChatResponse chat(String model, String message) throws OllamaAPIException {
		return chat(new OllamaChatRequest().model(model).addMessage(OllamaMessage.Role.user, message).stream(false));
	}

	default OllamaChatResponse chat(String model, OllamaMessage... messages) throws OllamaAPIException {
		return chat(new OllamaChatRequest().model(model).messages(messages).stream(false));
	}

	OllamaChatResponse chat(OllamaChatRequest chatRequest) throws OllamaAPIException;

	default StreamResponse<OllamaChatResponse> chatStream(String model, String message) throws OllamaAPIException {
		return chatStream(new OllamaChatRequest().model(model).addMessage(OllamaMessage.Role.user, message).stream(true));
	}

	default StreamResponse<OllamaChatResponse> chatStream(String model, OllamaMessage... messages) throws OllamaAPIException {
		return chatStream(new OllamaChatRequest().model(model).messages(messages).stream(true));
	}

	StreamResponse<OllamaChatResponse> chatStream(OllamaChatRequest chatRequest) throws OllamaAPIException;

	default OllamaGenerateResponse generate(String model, String prompt) throws OllamaAPIException {
		return generate(new OllamaGenerateRequest().model(model).prompt(prompt).stream(false));
	}

	OllamaGenerateResponse generate(OllamaGenerateRequest generateRequest) throws OllamaAPIException;

	default StreamResponse<OllamaGenerateResponse> generateStream(String model, String prompt) throws OllamaAPIException {
		return generateStream(new OllamaGenerateRequest().model(model).prompt(prompt).stream(true));
	}

	StreamResponse<OllamaGenerateResponse> generateStream(OllamaGenerateRequest generateRequest) throws OllamaAPIException;

	OllamaEmbedResponse.OfDouble embed(OllamaEmbedRequest embedRequest) throws OllamaAPIException;

	OllamaEmbedResponse.OfFloat embedFloats(OllamaEmbedRequest embedRequest) throws OllamaAPIException;

	default OllamaCreateModelResponse createModel(String name, String modelFile) throws OllamaAPIException {
		return createModel(new OllamaCreateModelRequest().name(name).modelfile(modelFile).stream(false));
	}

	default OllamaCreateModelResponse createModel(String name, ModelFile modelFile) throws OllamaAPIException {
		return createModel(new OllamaCreateModelRequest().name(name).modelfile(modelFile).stream(false));
	}

	OllamaCreateModelResponse createModel(OllamaCreateModelRequest createModelRequest) throws OllamaAPIException;

	default StreamResponse<OllamaCreateModelResponse> createModelStream(String name, String modelFile) throws OllamaAPIException {
		return createModelStream(new OllamaCreateModelRequest().name(name).modelfile(modelFile).stream(true));
	}

	default StreamResponse<OllamaCreateModelResponse> createModelStream(String name, ModelFile modelFile) throws OllamaAPIException {
		return createModelStream(new OllamaCreateModelRequest().name(name).modelfile(modelFile).stream(true));
	}

	StreamResponse<OllamaCreateModelResponse> createModelStream(OllamaCreateModelRequest createModelRequest) throws OllamaAPIException;

	OllamaListResponse list() throws OllamaAPIException;

	default OllamaShowResponse show(String name, boolean verbose) throws OllamaAPIException {
		return show(new OllamaShowRequest().name(name).verbose(verbose));
	}

	OllamaShowResponse show(OllamaShowRequest showRequest) throws OllamaAPIException;

	default boolean pullIfNotExists(String model) throws OllamaAPIException {
		if(!exists(model)) {
			pull(model);
			return true;
		}
		return false;
	}

	default OllamaPullResponse pull(String model) throws OllamaAPIException {
		return pull(new OllamaPullRequest().name(model).stream(false));
	}

	OllamaPullResponse pull(OllamaPullRequest pullRequest) throws OllamaAPIException;

	default StreamResponse<OllamaPullResponse> pullStream(String model) throws OllamaAPIException {
		return pullStream(new OllamaPullRequest().name(model).stream(true));
	}

	StreamResponse<OllamaPullResponse> pullStream(OllamaPullRequest pullRequest) throws OllamaAPIException;

	OllamaPushResponse push(OllamaPushRequest pushRequest) throws OllamaAPIException;

	StreamResponse<OllamaPushResponse> pushStream(OllamaPushRequest pushRequest) throws OllamaAPIException;

	OllamaPsResponse ps() throws OllamaAPIException;

	void copy(OllamaCopyRequest copyRequest) throws OllamaAPIException;

	default boolean exists(String model) throws OllamaAPIException {
		if(model == null) return false;
		model = model.replace(":latest", "");
		return list().modelNames().contains(model);
	}

	void delete(String name) throws OllamaAPIException;

	default boolean deleteIfExists(String name) throws OllamaAPIException {
		if(exists(name)) {
			delete(name);
			return true;
		}
		return false;
	}

	boolean existsBlob(String digest) throws OllamaAPIException;

	void createBlob(File file, String digest) throws OllamaAPIException, FileNotFoundException;

	void createBlob(File file) throws OllamaAPIException, FileNotFoundException;

	String baseUrl();

	Duration timeout();

	Ollama timeout(Duration timeout);

	Ollama setCommonHeader(String name, String value);

	void close();

	class StreamResponse<T> implements AutoCloseable, Iterator<T>, Iterable<T> {

		private final BufferedReader reader;
		private final Iterator<T> iterator;

		public StreamResponse(InputStream inputStream, Class<T> responseType) {
			this.reader = new BufferedReader(new InputStreamReader(inputStream));
			this.iterator = reader.lines().filter(l -> !l.isBlank()).map(l -> Json.fromJson(l, responseType)).iterator();
		}

		public Stream<T> stream() {
			return StreamSupport.stream(spliterator(), false);
		}

		public List<T> toList() {
			return stream().toList();
		}

		@Override
		public Iterator<T> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public T next() {
			return iterator.next();
		}

		@Override
		public void close() throws Exception {
			reader.close();
		}
	}

	static String sha256(File file) throws OllamaAPIException {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			try (DigestInputStream dis = new DigestInputStream(new FileInputStream(file), digest)) {
				byte[] buffer = new byte[8192];
				while (dis.read(buffer) != -1) {}
			}
			return bytesToHex(digest.digest());
		} catch (Exception e) {
			throw new OllamaAPIException(e);
		}
	}

	static String bytesToHex(byte[] hash) {
		try (Formatter formatter = new Formatter()) {
			for (byte b : hash) formatter.format("%02x", b);
			return formatter.toString();
		}
	}
}
