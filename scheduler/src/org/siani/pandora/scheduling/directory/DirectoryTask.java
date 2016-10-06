package org.siani.pandora.scheduling.directory;

import java.io.File;

public interface DirectoryTask {

	void execute(File file, PandoraDirectorySentinel.Event event);
}
