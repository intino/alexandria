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

public interface Ollama {

	static Ollama newClient() {
		return new OllamaHttpClient();
	}

	static Ollama newClient(String baseUrl) {
		return new OllamaHttpClient(baseUrl);
	}

	static Ollama newClient(String baseUrl, Duration timeout) {
		return new OllamaHttpClient(baseUrl, timeout);
	}

	OllamaChatResponse chat(String model, String message) throws OllamaAPIException;

	OllamaChatResponse chat(String model, OllamaMessage... messages) throws OllamaAPIException;

	OllamaChatResponse chat(OllamaChatRequest chatRequest) throws OllamaAPIException;

	StreamResponse<OllamaChatResponse> chatStream(String model, String message) throws OllamaAPIException;

	StreamResponse<OllamaChatResponse> chatStream(String model, OllamaMessage... messages) throws OllamaAPIException;

	StreamResponse<OllamaChatResponse> chatStream(OllamaChatRequest chatRequest) throws OllamaAPIException;

	OllamaGenerateResponse generate(String model, String prompt) throws OllamaAPIException;

	OllamaGenerateResponse generate(OllamaGenerateRequest generateRequest) throws OllamaAPIException;

	StreamResponse<OllamaGenerateResponse> generateStream(String model, String prompt) throws OllamaAPIException;

	StreamResponse<OllamaGenerateResponse> generateStream(OllamaGenerateRequest generateRequest) throws OllamaAPIException;

	OllamaEmbedResponse.OfDouble embed(OllamaEmbedRequest embedRequest) throws OllamaAPIException;

	OllamaEmbedResponse.OfFloat embedFloats(OllamaEmbedRequest embedRequest) throws OllamaAPIException;

	OllamaCreateModelResponse newClient(String name, String modelFile) throws OllamaAPIException;

	OllamaCreateModelResponse newClient(String name, ModelFile modelFile) throws OllamaAPIException;

	OllamaCreateModelResponse newClient(OllamaCreateModelRequest createModelRequest) throws OllamaAPIException;

	StreamResponse<OllamaCreateModelResponse> createStream(OllamaCreateModelRequest createModelRequest) throws OllamaAPIException;

	OllamaListResponse list() throws OllamaAPIException;

	OllamaShowResponse show(OllamaShowRequest showRequest) throws OllamaAPIException;

	OllamaShowResponse show(String name, boolean verbose) throws OllamaAPIException;

	boolean pullIfNotExists(String model) throws OllamaAPIException;

	OllamaPullResponse pull(String model) throws OllamaAPIException;

	OllamaPullResponse pull(OllamaPullRequest pullRequest) throws OllamaAPIException;

	StreamResponse<OllamaPullResponse> pullStream(String model) throws OllamaAPIException;

	StreamResponse<OllamaPullResponse> pullStream(OllamaPullRequest pullRequest) throws OllamaAPIException;

	OllamaPushResponse push(OllamaPushRequest pushRequest) throws OllamaAPIException;

	StreamResponse<OllamaPushResponse> pushStream(OllamaPushRequest pushRequest) throws OllamaAPIException;

	OllamaPsResponse ps() throws OllamaAPIException;

	void copy(OllamaCopyRequest copyRequest) throws OllamaAPIException;

	boolean exists(String model) throws OllamaAPIException;

	void delete(String name) throws OllamaAPIException;

	boolean deleteIfExists(String name) throws OllamaAPIException;

	boolean existsBlob(String digest) throws OllamaAPIException;

	void createBlob(File file, String digest) throws OllamaAPIException, FileNotFoundException;

	void createBlob(File file) throws OllamaAPIException, FileNotFoundException;

	String baseUrl();

	Duration timeout();

	OllamaHttpClient timeout(Duration timeout);

	Map<String, String> commonHeaders();

	OllamaHttpClient setCommonHeaders(Map<String, String> headers);

	String getCommonHeader(String name);

	OllamaHttpClient setCommonHeader(String name, String value);

	OllamaHttpClient removeCommonHeader(String name);

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
