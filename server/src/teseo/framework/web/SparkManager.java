package teseo.framework.web;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class SparkManager {


	private final Request request;
	private final Response response;

	public SparkManager(Request request, Response response) {
		this.request = request;
		this.response = response;
	}

	public void write(Object object) {
		new SparkWriter(response).write(object);
	}

	public <T> T fromHeader(String name, Class<T> type) {
		return (T) request.headers(name);
	}

	public <T> T fromQuery(String name, Class<T> type) {
		return (T) request.queryParams(name);
	}

	public <T> T fromPath(String name, Class<T> schema) {
		return (T) request.params(name);
	}

	public <T> T fromBody(String name, Class<T> schema) {
		return new Gson().fromJson(request.body(), schema);
	}

	public Object fromForm(String name) {
		return readPart(name);
	}

	public Object fromForm(String name, Class schema) {
		final Object o = readPart(name);
		return o != null ? new Gson().fromJson(o.toString(), schema) : null;
	}

	private Object readPart(String method) {
		try {
			Part part = request.raw().getPart(method);
			if (part.getContentType() == null)
				return readString(part.getInputStream());
			return part.getInputStream();
		} catch (IOException | ServletException e) {
			return null;
		}
	}

	private String readString(InputStream stream) throws IOException {
		Scanner scanner = new Scanner(stream, "UTF-8").useDelimiter("\\A");
		return scanner.hasNext() ? scanner.next() : "";
	}
}
