package io.intino.alexandria;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Base64;

import static java.net.URLConnection.getFileNameMap;
import static java.net.URLConnection.guessContentTypeFromName;
import static java.net.URLConnection.guessContentTypeFromStream;

public class Resource {
	private final String name;
	private final String extension;
	private byte[] data;

	public Resource(String name, byte[] data) {
		this.name = name;
		this.extension = name.substring(name.lastIndexOf('.') + 1);
		this.data = data;
	}

	public Resource(String name, InputStream data) throws IOException {
		this(name, read(data));
	}

    public String name() {
        return name;
    }

    public String type() {
        return extension;
    }

    public byte[] data() {
        return data;
    }

    public InputStream stream() {
        return new ByteArrayInputStream(data);
    }

    private static byte[] read(InputStream is) throws IOException {
        byte[] buffer = new byte[4096];
        ByteBuffer bb = ByteBuffer.allocate(3);
        while (is.available() > 0) {
            int length = is.read(buffer);
            bb.put(buffer,0,length);
        }
        return bb.array();
    }

	public Metadata metadata() {
	    return new Metadata(this);
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

    public void data(byte[] data) {
        this.data = data;
    }

    public static class Metadata {
        private final Resource resource;
        private final String contentType;
        private final String mimeType;

        Metadata(Resource resource) {
            this.resource = resource;
            this.contentType = guessContentTypeFromName(resource.name);
            this.mimeType = getFileNameMap().getContentTypeFor(resource.name);

        }

        public String mimeType() {
            return mimeType;
        }

        public String contentType() {
            return contentType != null ? contentType : resolveContentType();
        }

        private String resolveContentType() {
            try {
                return guessContentTypeFromStream(new ByteArrayInputStream(resource.data));
            } catch (IOException e) {
                return null;
            }
        }


    }
}
