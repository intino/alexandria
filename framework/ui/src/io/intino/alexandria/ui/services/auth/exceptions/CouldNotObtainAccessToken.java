package io.intino.alexandria.ui.services.auth.exceptions;

public class CouldNotObtainAccessToken extends Throwable {
	public CouldNotObtainAccessToken(Exception exception) {
		super(exception);
	}
}
