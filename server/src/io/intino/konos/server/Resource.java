package io.intino.konos.server;

import io.intino.konos.exceptions.KonosException;

public interface Resource {
	void execute() throws KonosException;
}
