package io.intino.alexandria.scheduling.directory;

import java.io.File;

public interface DirectoryTask {

	void execute(File file, KonosDirectorySentinel.Event event);
}
