package org.siani.pandora.server;

import org.siani.pandora.exceptions.PandoraException;

public interface Resource {
	void execute() throws PandoraException;
}
