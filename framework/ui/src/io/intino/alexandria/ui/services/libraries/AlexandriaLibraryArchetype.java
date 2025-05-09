package io.intino.alexandria.ui.services.libraries;

import java.io.File;

public class AlexandriaLibraryArchetype {
	private final File root;
	private final String rootPackage;
	private final String name;

	public AlexandriaLibraryArchetype(File root, String rootPackage, String libraryName) {
		this.root = root;
		this.rootPackage = rootPackage;
		this.name = libraryName;
	}

	public File file() {
		return new File(root, filename());
	}

	public File directory() {
		return new File(root, withoutExtension(filename()));
	}

	public File webDirectory() {
		return new File(directory(), "www");
	}

	public String rootPackage() {
		return rootPackage;
	}

	public String libraryName() {
		return name;
	}

	private String withoutExtension(String name) {
		if (!name.contains(".")) return name;
		return name.substring(0, name.lastIndexOf("."));
	}

	private String filename() {
		return name.toLowerCase() + ".jar";
	}

}
