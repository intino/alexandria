package org.siani.pandora.server.web;

import org.siani.pandora.exceptions.PandoraException;

public interface Resource {

	void execute() throws PandoraException;
}
