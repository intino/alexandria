package io.intino.alexandria.rest.security;

public interface BasicAuthenticationValidator {

	boolean validate(String token);
}
