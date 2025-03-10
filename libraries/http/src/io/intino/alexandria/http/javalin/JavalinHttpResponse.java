package io.intino.alexandria.http.javalin;

import io.intino.alexandria.http.server.AlexandriaHttpResponse;
import io.intino.alexandria.logger.Logger;
import io.javalin.http.Context;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

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
	public void error(int code, String message) {
		try {
			context.res().setStatus(code);
			context.res().getWriter().write(message);
			if (message.startsWith("{")) context.res().setContentType("application/json");
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	@Override
	public HttpServletResponse raw() {
		return context.res();
	}
}
