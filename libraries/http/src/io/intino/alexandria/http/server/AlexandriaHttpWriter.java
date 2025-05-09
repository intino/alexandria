package io.intino.alexandria.http.server;

import io.intino.alexandria.Resource;
import io.intino.alexandria.exceptions.AlexandriaError;
import io.intino.alexandria.http.MimeTypes;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;

class AlexandriaHttpWriter {
	private final AlexandriaHttpResponse response;

	AlexandriaHttpWriter(AlexandriaHttpResponse response) {
		this.response = response;
	}

	void write(Object object) {
		write(object, null);
	}

	void write(Object object, String name) {
		write(object, name, false);
	}

	void write(Object object, String name, boolean embedded) {
		if (object instanceof File) writeFile((File) object, embedded);
		else if (object instanceof Resource) writeResource((Resource) object, embedded);
		else if (object instanceof InputStream) writeStream((InputStream) object, name, embedded);
		else if (object instanceof byte[]) writeBytes((byte[]) object, name, embedded);
		else writeResponse(object.toString(), name);
	}

	void writeHeader(String name, String value) {
		response.header(name, value != null ? value : "");
	}

	private void writeResponse(String message, String contentType) {
		try {
			writeResponse(message, contentType, response.raw());
		} catch (IOException ignored) {
		}
	}

	public void writeError(AlexandriaError response, String message) {
		try {
			writeResponseError(response.code(), message, this.response.raw());
		} catch (IOException ignored) {
		}
	}

	private void writeFile(File file, boolean embedded) {
		writeResponse(file, embedded, response.raw());
	}

	private void writeStream(InputStream stream, String filename, boolean embedded) {
		writeResponse(filename, stream, embedded, response.raw());
	}

	private void writeResource(Resource resource, boolean embedded) {
		writeResponse(resource, embedded, response.raw());
	}

	private void writeBytes(byte[] content, String filename, boolean embedded) {
		if (filename == null) filename = "default.bin";
		writeResponse(filename, content, embedded, response.raw());
	}

	private void writeResponse(String content, String contentType, HttpServletResponse response) throws IOException {
		//response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("X-Content-Type-Options", "nosniff");
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		response.setContentType(contentType != null ? contentType : "text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		writer.print(content);
		writer.close();
	}

	private void writeResponse(File file, boolean embedded, HttpServletResponse response) {
		try {
			response.setContentType(MimeTypes.getFromFile(file));
			response.setHeader("Content-Disposition", (embedded ? "inline" : "attachment") + ";filename=\"" + file.getName() + "\"");
			writeResponse(readFile(file), response);
		} catch (IOException ignored) {
		}
	}

	private void writeResponse(Resource resource, boolean embedded, HttpServletResponse response) {
		try {
			String filename = resource.name();
			String contentType = resource.metadata().contentType();
			response.setContentType(contentType);
			resource.metadata().properties().forEach(response::setHeader);
			response.setHeader("Content-Disposition", (embedded ? "inline" : "attachment") + ";filename=\"" + (filename != null ? filename : ("resource." + MimeTypes.getExtension(contentType))) + "\"");
			writeResponseStream(resource.stream(), response);
		} catch (IOException ignored) {
		}
	}

	private void writeResponse(String filename, InputStream stream, boolean embedded, HttpServletResponse response) {
		try {
			String contentType = filename != null ? MimeTypes.getFromFilename(filename) : "application/octet-stream";
			response.setContentType(contentType);
			response.setHeader("Content-Disposition", (embedded ? "inline" : "attachment") + ";filename=\"" + (filename != null ? filename : ("resource." + MimeTypes.getExtension(contentType))) + "\"");
			writeResponseStream(stream, response);
		} catch (IOException ignored) {
		}
	}

	private void writeResponseStream(InputStream stream, HttpServletResponse response) throws IOException {
		try (stream) {
			long size = stream.transferTo(response.getOutputStream());
			response.setContentLengthLong(size);
			response.getOutputStream().flush();
		}
	}

	private void writeResponse(String filename, byte[] content, boolean embedded, HttpServletResponse response) {
		response.setContentType(MimeTypes.getFromFilename(filename));
		response.setHeader("Content-Disposition", (embedded ? "inline" : "attachment") + "attachment; filename=\"" + filename + "\"");
		this.writeResponse(content, response);
	}

	private void writeResponse(byte[] content, HttpServletResponse response) {
		try {
			response.setContentLength(content.length);
			response.getOutputStream().write(content);
			response.getOutputStream().flush();
		} catch (IOException ignored) {
		}
	}

	private void writeResponseError(String code, String error, HttpServletResponse response) throws IOException {
		response.setStatus(Integer.parseInt(code));
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.getWriter().print(error);
		response.flushBuffer();
	}

	private byte[] readFile(File file) throws IOException {
		return Files.readAllBytes(file.toPath());
	}
}
