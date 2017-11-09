package io.intino.konos.scheduling.directory;

import java.io.File;

public interface DirectoryTask {

	void execute(File file, KonosDirectorySentinel.Event event);
}
