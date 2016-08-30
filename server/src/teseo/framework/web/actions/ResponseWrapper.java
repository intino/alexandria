package teseo.framework.web.actions;

import spark.Response;
import spark.utils.IOUtils;
import teseo.framework.actions.ResponseAdapter;
import teseo.framework.web.utils.MimeTypes;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

class ResponseWrapper extends SparkWrapper {

	private static final String Redirect = "redirect";
	private final Response response;

	ResponseWrapper(Response response) {
		this.response = response;
	}

	@Override
	protected InvocationHandler handler() {
		return (proxy, method, args) -> {

			if (method.getName().equals(Redirect))
				redirect((URL)args[0]);
			else
				write(method, args);

			return "OK";
		};
	}

	private void redirect(URL url) {
		response.redirect(url.toString(), HttpServletResponse.SC_MOVED_TEMPORARILY);
	}

	@SuppressWarnings("unchecked")
	private void write(Method method, Object[] args) throws IOException {
		Object response = args[0];

		if (response instanceof Error) {
			writeError(adapterFor(method).adapt(response));
		} else if (response instanceof File) {
			writeFile((File) response);
		} else if (response instanceof InputStream) {
			writeStream((InputStream) response, args.length>1?(String)args[1]:null);
		} else if (response instanceof byte[]) {
			writeBytes((byte[]) response, args.length>1?(String)args[1]:null);
		} else {
			writeResponse(adaptResponse(method, response), args.length > 1 ? (String) args[1] : null);
		}
	}

	@SuppressWarnings("unchecked")
	private String adaptResponse(Method method, Object response) {
		return response instanceof List ? adapterFor(method).adaptList((List) response) :
				adapterFor(method).adapt(response);
	}

	private void writeResponse(String message, String contentType) throws IOException {
		writeResponse(message, contentType, response.raw());
	}

	private void writeError(String message) throws IOException {
		writeResponseError(message, response.raw());
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

	private void writeResponseError(String error, HttpServletResponse response) throws IOException {
		response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.getWriter().print(error);
		response.flushBuffer();
	}

	private ResponseAdapter adapterFor(Method method) {
		return adapters.responseAdapterOf(method.getName());
	}

	private byte[] readFile(File file) throws IOException {
		return Files.readAllBytes(file.toPath());
	}
}
