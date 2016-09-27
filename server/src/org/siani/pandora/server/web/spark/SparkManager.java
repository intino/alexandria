package org.siani.pandora.server.web.spark;

import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.IOException;

import static org.siani.pandora.server.web.spark.SparkReader.read;

@SuppressWarnings("unchecked")
public class SparkManager {


	private final Request request;
	private final Response response;

	public SparkManager(Request request, Response response) {
		this.request = request;
		this.response = response;
		setUpMultipartConfiguration();
	}

	public void write(Object object) {
		new SparkWriter(response).write(object);
	}

	public <T> T fromHeader(String name, Class<T> type) {
		return read(request.headers(name), type);
	}

	public <T> T fromQuery(String name, Class<T> type) {
		return read(request.queryParams(name), type);
	}

	public <T> T fromPath(String name, Class<T> type) {
		return read(request.params(name), type);
	}

	public <T> T fromBody(String name, Class<T> type) {
		return read(request.body(), type);
	}

	public <T> T fromForm(String name, Class<T> type) {
		return readPart(type);
	}

	private <T> T readPart(Class<T> type) {
		try {
			Part part = request.raw().getPart("content");
			if (part == null) return null;
			return read(part.getInputStream(), type);
		} catch (IOException | ServletException e) {
			return null;
		}
	}

	private void setUpMultipartConfiguration() {
		MultipartConfigElement multipartConfigElement = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));
		request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
	}
}
