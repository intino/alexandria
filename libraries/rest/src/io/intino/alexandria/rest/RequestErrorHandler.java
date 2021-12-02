package io.intino.alexandria.rest;

import io.intino.alexandria.exceptions.AlexandriaException;

public interface RequestErrorHandler {

	void onMalformedRequest(Throwable e) throws AlexandriaException;
}
