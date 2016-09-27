package org.siani.pandora.server.web.spark;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.siani.pandora.Error;
import spark.Response;
import spark.utils.IOUtils;
import org.siani.pandora.server.web.utils.MimeTypes;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Map;

@SuppressWarnings("Duplicates")
class SparkWriter {
	private final Response response;

	SparkWriter(Response response) {
		this.response = response;
	}


	public void write(Object object) {
		write(object, null);
	}

	public void write(Object object, String name) {
		if (object instanceof Error) writeError((Error) object, adapt(object));
		else if (object instanceof File) writeFile((File) object);
		else if (object instanceof InputStream)
			writeStream((InputStream) object, name);
		else if (object instanceof byte[]) {
			writeBytes((byte[]) object, name);
		} else writeResponse(adapt(object), name);
	}

	private String adapt(Object object) {
		if (object instanceof Error) return adaptError((Error) object);
		if (object instanceof Collection) return jsonArray((Collection<Object>) object);
		if (object instanceof String) return (String) object;
		return toJson(object).toString();
	}


	private String jsonArray(Collection<Object> objects) {
		JsonArray result = new JsonArray();
		for (Object value : objects)
			result.add(toJson(value));
		return result.toString();
	}

	private JsonElement toJson(Object value) {
		return new Gson().toJsonTree(value);
	}


	private void writeResponse(String message, String contentType) {
		try {
			writeResponse(message, contentType, response.raw());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeError(Error response, String message) {
		try {
			writeResponseError(response.code(), message, this.response.raw());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeFile(File file) {
		writeResponse(file, response.raw());
	}

	private void writeStream(InputStream stream, String filename) {
		writeResponse(filename, stream, response.raw());
	}

	private void writeBytes(byte[] content, String filename) {
		if (filename == null) filename = "default.bin";
		writeResponse(filename, content, response.raw());
	}

	private void writeResponse(String content, String contentType, HttpServletResponse response) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		response.setContentType(contentType != null ? contentType : "text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		writer.println(content);
		writer.close();
	}

	private void writeResponse(File file, HttpServletResponse response) {
		try {
			response.setContentType(MimeTypes.getFromFile(file));
			response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
			writeResponse(readFile(file), response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeResponse(String filename, InputStream stream, HttpServletResponse response) {
		try {
			byte[] content = IOUtils.toByteArray(stream);
			String contentType = filename != null ? MimeTypes.getFromFilename(filename) : MimeTypes.getFromStream(new ByteArrayInputStream(content));
			response.setContentType(contentType);
			response.setHeader("Content-Disposition", "attachment; filename=" + (filename != null ? filename : ("resource." + MimeTypes.getExtension(contentType))));
			writeResponse(content, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeResponse(String filename, byte[] content, HttpServletResponse response) {
		response.setContentType(MimeTypes.getFromFilename(filename));
		response.setHeader("Content-Disposition", "attachment; filename=" + filename);
		this.writeResponse(content, response);
	}

	private void writeResponse(byte[] content, HttpServletResponse response) {
		try {
			response.setContentLength(content.length);
			response.getOutputStream().write(content);
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeResponseError(String code, String error, HttpServletResponse response) throws IOException {
		response.setStatus(Integer.parseInt(code));
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.getWriter().print(error);
		response.flushBuffer();
	}

	private String adaptError(Error error) {
		JsonObject object = new JsonObject();
		object.addProperty("code", error.code());
		object.addProperty("label", error.label());
		adaptParameters(object, error.parameters());

		return object.toString();
	}

	private void adaptParameters(JsonObject object, Map<String, String> parameters) {
		for (Map.Entry<String, String> parameter : parameters.entrySet())
			object.addProperty(parameter.getKey(), parameter.getValue());
	}

	private byte[] readFile(File file) throws IOException {
		return Files.readAllBytes(file.toPath());
	}
}
