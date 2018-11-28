package io.intino.alexandria.ui.services.auth.exceptions;

public class CouldNotInvalidateAccessToken extends Throwable {
	public CouldNotInvalidateAccessToken(Exception exception) {
		super(exception);
	}
}
