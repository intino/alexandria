package io.intino.konos.alexandria.rest.spark;

import io.intino.konos.alexandria.schema.Resource;
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
		write(object, name, false);
	}

	public void write(Object object, String name, boolean embedded) {
		new SparkWriter(response).write(object, name, embedded);
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
		try {
			Part part = request.raw().getPart(name);
			return (T) new Resource(part.getName()).data(part.getInputStream()).contentType(part.getContentType());
		} catch (ServletException | IOException e) {
			return null;
		}
	}

	public Request request() {
		return request;
	}

	public Response response() {
		return response;
	}

	private void setUpMultipartConfiguration() {
		MultipartConfigElement multipartConfigElement = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));
		request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
	}
}
