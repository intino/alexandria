package io.intino.alexandria.http.server;

import jakarta.servlet.http.HttpServletResponse;

public interface AlexandriaHttpResponse {
	void header(String name, String value);
	void status(int status);
	void removeCookie(String name);

	HttpServletResponse raw();
}
