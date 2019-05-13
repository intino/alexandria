package io.intino.alexandria.rest;

import io.intino.alexandria.exceptions.AlexandriaException;

public interface Resource {
	void execute() throws AlexandriaException;
}
