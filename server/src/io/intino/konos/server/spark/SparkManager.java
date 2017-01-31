package io.intino.konos.server.spark;

import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.IOException;

@SuppressWarnings("unchecked")
public class SparkManager {
	protected final Request request;
	protected final Response response;

	public SparkManager(Request request, Response response) {
		this.request = request;
		this.response = response;
		setUpMultipartConfiguration();
	}

	public void write(Object object) {
		new SparkWriter(response).write(object);
	}

	public void write(Object object, String name) {
		new SparkWriter(response).write(object, name);
	}

	public <T> T fromHeader(String name, Class<T> type) {
		return SparkReader.read(request.headers(name), type);
	}

	public <T> T fromQuery(String name, Class<T> type) {
		return SparkReader.read(request.queryParams(name), type);
	}

	public <T> T fromPath(String name, Class<T> type) {
		return SparkReader.read(request.params(name), type);
	}

	public <T> T fromBody(String name, Class<T> type) {
		return SparkReader.read(request.body(), type);
	}

	public <T> T fromForm(String name, Class<T> type) {
		return readPart(type);
	}

	private <T> T readPart(Class<T> type) {
		try {
			Part part = request.raw().getPart("content");
			if (part == null) return null;
			return SparkReader.read(part.getInputStream(), type);
		} catch (IOException | ServletException e) {
			return null;
		}
	}

	private void setUpMultipartConfiguration() {
		MultipartConfigElement multipartConfigElement = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));
		request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
	}

}
