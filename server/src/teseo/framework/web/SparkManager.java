package teseo.framework.web;

import spark.Request;
import spark.Response;

import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

@SuppressWarnings("unchecked")
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
		Object o = readPart(name);
		return o instanceof String ? SparkReader.read(o.toString(), type) : (T) o;
	}

	private Object readPart(String method) {
		try {
			Part part = request.raw().getPart(method);
			if (part.getContentType() == null)
				return readStream(part.getInputStream());
			return part.getInputStream();
		} catch (IOException | ServletException e) {
			return null;
		}
	}

	private String readStream(InputStream stream) throws IOException {
		Scanner scanner = new Scanner(stream, "UTF-8").useDelimiter("\\A");
		return scanner.hasNext() ? scanner.next() : "";
	}
}
