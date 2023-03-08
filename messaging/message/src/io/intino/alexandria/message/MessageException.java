package io.intino.alexandria.message;

public class MessageException extends RuntimeException {
	public MessageException(String message, Exception e) {
		super(message, e);
	}
}
