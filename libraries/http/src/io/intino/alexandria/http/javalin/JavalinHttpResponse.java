package io.intino.alexandria.http.javalin;

import io.intino.alexandria.http.server.AlexandriaHttpResponse;
import io.javalin.http.Context;
import jakarta.servlet.http.HttpServletResponse;

public class JavalinHttpResponse implements AlexandriaHttpResponse {
	private final Context context;

	public JavalinHttpResponse(Context context) {
		this.context = context;
	}

	@Override
	public void header(String name, String value) {
		context.header(name, value);
	}

	@Override
	public void status(int status) {
		context.status(status);
	}

	@Override
	public void removeCookie(String name) {
		context.removeCookie(name);
	}

	@Override
	public HttpServletResponse raw() {
		return context.res();
	}
}
