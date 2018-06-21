package io.intino.konos.alexandria.rest.security;

public interface BasicAuthenticationValidator {

	boolean validate(String token);
}
