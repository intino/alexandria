package io.intino.konos.alexandria.rest;

import io.intino.konos.alexandria.exceptions.AlexandriaException;

public interface Resource {
	void execute() throws AlexandriaException;
}
