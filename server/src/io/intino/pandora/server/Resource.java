package io.intino.pandora.server;

import io.intino.pandora.exceptions.PandoraException;

public interface Resource {
	void execute() throws PandoraException;
}
