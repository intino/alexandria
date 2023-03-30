package io.intino.alexandria;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static java.net.URLConnection.*;
import static java.util.Objects.requireNonNull;

public class Resource {

	private final String name;
	private final Metadata metadata;
	private final InputStreamProvider inputStreamProvider;

	public Resource(File file) {
		this(file.getName(), file);
	}

	public Resource(String name, File file) {
		this(name, InputStreamProvider.of(requireNonNull(file, "File cannot be null")));
	}

	public Resource(String name, URL url) {
		this(name, InputStreamProvider.of(requireNonNull(url, "URL cannot be null")));
	}

	public Resource(String name, byte[] bytes) {
		this(name, null, InputStreamProvider.of(requireNonNull(bytes, "Byte array cannot be null")));
	}

	public Resource(String name, String contentType, byte[] bytes) {
		this(name, contentType, InputStreamProvider.of(requireNonNull(bytes, "Byte array cannot be null")));
	}

	public Resource(String name, InputStream inputStream) {
		this(name, null, requireNonNull(inputStream, "InputStream cannot be null"));
	}

	public Resource(String name, InputStreamProvider inputStreamProvider) {
		this(name, null, inputStreamProvider);
	}

	public Resource(String name, String contentType, InputStream inputStream) {
		this(name, contentType, InputStreamProvider.of(requireNonNull(inputStream, "InputStream cannot be null")));
	}

	public Resource(String name, String contentType, InputStreamProvider inputStreamProvider) {
		this.name = complete(name, contentType);
		this.inputStreamProvider = requireNonNull(inputStreamProvider, "InputStreamProvider cannot be null");
		this.metadata = new Metadata(contentType);
	}

	public String name() {
		return name;
	}

	public String type() {
		return this.name.substring(name.lastIndexOf('.') + 1);
	}

	public boolean isBackedInMemory() {
		return inputStreamProvider.isFromMemory();
	}

	public String readAsString() throws IOException {
		return readAsString(Charset.defaultCharset());
	}

	public String readAsString(Charset charset) throws IOException {
		return new String(readAllBytes(), charset);
	}

	public byte[] bytes() throws IOException {
		return readAllBytes();
	}

	public byte[] readAllBytes() throws IOException {
		try(InputStream inputStream = open()) {
			return inputStream.readAllBytes();
		}
	}

	public InputStream open() throws IOException {
		return inputStreamProvider.get();
	}

	public InputStream inputStream() throws IOException {
		return open();
	}

	public InputStream stream() throws IOException {
		return open();
	}

	public SafeReader.Builder safeReader() {
		return new SafeReader.Builder(inputStreamProvider);
	}

	public Metadata metadata() {
		return metadata;
	}

	@Override
	public String toString() {
		return name();
	}

	private String complete(String name, String contentType) {
		if (contentType == null) return name;
		String extension = extensionOf(contentType);
		return name + (name.endsWith(extension) ? "" : extension);
	}

	private String extensionOf(String contentType) {
		return contentType == null ? "" : MimeTypes.extensionOf(contentType);
	}

	public class Metadata {
		private final Map<String, String> properties = new LinkedHashMap<>(4);

		private Metadata(String contentType) {
			properties.put("mimeType", getFileNameMap().getContentTypeFor(Resource.this.name));
			properties.put("contentType", contentType == null ? guessContentTypeFromName(Resource.this.name) : contentType);
        }

        public String mimeType() {
            return properties.get("mimeType");
        }

        public String contentType() {
            return properties.getOrDefault("contentType", resolveContentType());
        }

		public Resource contentType(String contentType) {
			properties.put("contentType", contentType);
			return Resource.this;
		}

		public void putAll(Map<String, String> properties) {
			properties.forEach(this::setProperty);
		}

		public Metadata setProperty(String name, String value) {
			if(name == null || value == null) return this;
			properties.put(name, value);
			return this;
		}

		public Optional<String> getProperty(String property) {
			return Optional.ofNullable(properties.get(property));
		}

		public Map<String, String> properties() {
			return Collections.unmodifiableMap(properties);
		}

		private String resolveContentType() {
            try {
				return inputStreamProvider.isFromMemory() ? guessContentTypeFromStream(inputStreamProvider.get()) : null;
			} catch (IOException e) {
                return null;
            }
        }

		@Override
		public String toString() {
			return properties.toString();
		}
	}

	@FunctionalInterface
	public interface InputStreamProvider {

		default boolean isFromMemory() {
			return false;
		}

		InputStream get() throws IOException;

		static InputStreamProvider of(File file) {
			return() -> new BufferedInputStream(new FileInputStream(file));
		}

		static InputStreamProvider of(URL url) {
			return url::openStream;
		}

		static InputStreamProvider of(URI uri) throws MalformedURLException {
			return InputStreamProvider.of(uri.toURL());
		}

		static InputStreamProvider of(InputStream inputStream) {
			return new InputStreamProvider() {
				@Override
				public InputStream get() {
					return inputStream;
				}

				@Override
				public boolean isFromMemory() {
					return inputStream instanceof ByteArrayInputStream;
				}
			};
		}

		static InputStreamProvider of(byte[] bytes) {
			return new InputStreamProvider() {
				@Override
				public InputStream get() {
					return new ByteArrayInputStream(bytes) {
						@Override
						public byte[] readAllBytes() {
							return buf;
						}
					};
				}
				@Override
				public boolean isFromMemory() {
					return true;
				}
			};
		}
	}

	public static class SafeReader {

		private final InputStreamProvider inputStreamProvider;
		private final InputStream defaultInputStream;
		private final String defaultString;
		private final byte[] defaultByteArray;

		public SafeReader(InputStreamProvider inputStreamProvider, InputStream defaultInputStream, String defaultString, byte[] defaultByteArray) {
			this.inputStreamProvider = inputStreamProvider;
			this.defaultInputStream = defaultInputStream;
			this.defaultString = defaultString;
			this.defaultByteArray = defaultByteArray;
		}

		public String readAsString() {
			return readAsString(Charset.defaultCharset());
		}

		public String readAsString(Charset charset) {
			try {
				return new String(inputStreamProvider.get().readAllBytes(), charset);
			} catch (Exception ignored) {
				return defaultString;
			}
		}

		public byte[] bytes() {
			return readAllBytes();
		}

		public byte[] readAllBytes() {
			try(InputStream inputStream = inputStreamProvider.get()) {
				return inputStream.readAllBytes();
			} catch (Exception ignored) {
				return defaultByteArray;
			}
		}

		public InputStream open() {
			try {
				return inputStreamProvider.get();
			} catch (IOException ignored) {
				return defaultInputStream;
			}
		}

		public InputStream inputStream() {
			return open();
		}

		public InputStream stream() {
			return open();
		}

		public static class Builder {
			private final InputStreamProvider provider;

			private Builder(InputStreamProvider provider) {
				this.provider = provider;
			}

			public SafeReader ofNull() {
				return new SafeReader(provider, null, null, null);
			}

			public SafeReader ofEmpty() {
				return new SafeReader(provider, InputStream.nullInputStream(), "", new byte[0]);
			}
		}
	}
}
