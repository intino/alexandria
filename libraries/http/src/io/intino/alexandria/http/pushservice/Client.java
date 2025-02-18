package io.intino.alexandria.http.pushservice;

public interface Client {
	String id();

	String sessionId();

	String language();

	int timezoneOffset();

	void language(String language);

	boolean send(String message);

	void destroy();
}
