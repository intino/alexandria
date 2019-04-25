package io.intino.alexandria.scheduler.directory;

import java.io.File;

public interface DirectoryTask {

	void execute(File file, DirectorySentinel.Event event);
}
