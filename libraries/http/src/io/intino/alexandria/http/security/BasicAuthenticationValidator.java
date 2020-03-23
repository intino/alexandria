package io.intino.alexandria.http.security;

public interface BasicAuthenticationValidator {

	boolean validate(String token);
}
