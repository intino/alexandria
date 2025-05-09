package io.intino.alexandria.ui.services.libraries;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.ui.utils.ZipHelper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class AlexandriaLibraryInstaller {
	private final AlexandriaLibraryArchetype archetype;

	public AlexandriaLibraryInstaller(AlexandriaLibraryArchetype archetype) {
		super();
		this.archetype = archetype;
	}

	public void install(File library) {
		if (sameLibrary(library) && extracted(library)) return;
		removeCurrentLibrary();
		copy(library);
		extract(library);
	}

	private boolean sameLibrary(File library) {
		if (library == null) return true;
		return archetype.file().exists() && archetype.file().lastModified() == library.lastModified();
	}

	private boolean extracted(File library) {
		if (library == null) return true;
		File extractDirectory = archetype.directory();
		if (!extractDirectory.exists()) return false;
		File[] files = extractDirectory.listFiles();
		return files != null && files.length > 0;
	}

	private void removeCurrentLibrary() {
		try {
			if (archetype.file().exists()) FileUtils.delete(archetype.file());
			if (archetype.directory().exists()) FileUtils.deleteDirectory(archetype.directory());
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private void copy(File library) {
		try {
			Files.copy(library.toPath(), archetype.file().toPath());
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private void extract(File library) {
		ZipHelper.extract(library, archetype.directory());
	}

}