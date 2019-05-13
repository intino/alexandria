package io.intino.alexandria;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static java.net.URLConnection.*;

public class Resource {
	private final String name;
	private final String extension;
	private final Metadata metadata;
	private final InputStream inputStream;

	public Resource(String name, byte[] bytes) {
		this(name, null, new ResourceInputStream(bytes));
	}

	public Resource(String name, String contentType, byte[] bytes) {
		this(name, contentType, new ResourceInputStream(bytes));
	}

	public Resource(String name, InputStream inputStream) {
		this(name, null, inputStream);
	}

	public Resource(String name, String contentType, InputStream inputStream) {
		this.name = complete(name, contentType);
		this.extension = this.name.substring(name.lastIndexOf('.') + 1);
		this.inputStream = inputStream;
		this.metadata = new Metadata(contentType);
	}

	private String complete(String name, String contentType) {
		if (contentType == null) return name;
		String extension = extensionOf(contentType);
		return name + (name.endsWith(extension) ? "" : extension);
	}

	private String extensionOf(String contentType) {
		if (contentType == null) return "";
		throw new RuntimeException("TODO. No te olvides de implementarlo");
		//return ".pdf";
	}

	public String name() {
		return name;
	}

	public String type() {
		return extension;
	}

	public byte[] bytes() {
		return inputStream instanceof ResourceInputStream ? resourceInputStream().bytes() : read(stream());
	}

	public InputStream stream() {
		return inputStream instanceof ResourceInputStream ? resourceInputStream() : inputStream;
	}

	private ResourceInputStream resourceInputStream() {
		ResourceInputStream stream = (ResourceInputStream) this.inputStream;
		stream.reset();
		return stream;
	}

	public Metadata metadata() {
		return metadata;
	}

	private static byte[] read(InputStream is) {
		try {
			byte[] buffer = new byte[4096];
			ByteBuffer bb = ByteBuffer.allocate(0);
			while (is.available() > 0) {
				int length = is.read(buffer);
				bb.put(buffer, 0, length);
			}
			return bb.array();
		} catch (Exception ignored) {
			return null;
		}
    }

	@Override
	public String toString() {
		return name;
	}

    public static Resource parse(String line) {
	    int i = line.indexOf('@');
	    String name = i >= 0 ? line.substring(0,i) : line;
        byte[] data = i >= 0 ? line.substring(i+1).getBytes() : new byte[0];
        return new Resource(name, data);
    }

	public class Metadata {
		private String mimeType;
		private String contentType;

		Metadata(String contentType) {
			this.mimeType = getFileNameMap().getContentTypeFor(Resource.this.name);
			this.contentType = contentType == null ? guessContentTypeFromName(Resource.this.name) : contentType;
        }

        public String mimeType() {
            return mimeType;
        }

        public String contentType() {
            return contentType != null ? contentType : resolveContentType();
        }

		public Resource contentType(String contentType) {
			this.contentType = contentType;
			return Resource.this;
		}

        private String resolveContentType() {
            try {
				return inputStream instanceof ByteArrayInputStream ? guessContentTypeFromStream(Resource.this.stream()) : null;
			} catch (IOException e) {
                return null;
            }
        }


    }

	private static class ResourceInputStream extends ByteArrayInputStream {
		ResourceInputStream(byte[] buf) {
			super(buf);
		}

		byte[] bytes() {
			return buf;
		}
	}

}
