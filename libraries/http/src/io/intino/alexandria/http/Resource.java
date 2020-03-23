package io.intino.alexandria.http;

import io.intino.alexandria.exceptions.AlexandriaException;

public interface Resource {
	void execute() throws AlexandriaException;
}
