package io.intino.alexandria.terminal;

import java.io.File;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class OutBox {
	protected final File directory;
	protected List<File> files;

	public OutBox(File directory) {
		this.directory = directory.getAbsoluteFile();
		this.files = new CopyOnWriteArrayList<>();
		if (!directory.exists()) directory.mkdirs();
		else reloadOutBox();
	}

	void pop() {
		reloadOutBox();
		if (files.isEmpty()) return;
		files.get(0).delete();
		files.remove(0);
	}

	boolean isEmpty() {
		return reloadOutBox().isEmpty();
	}

	protected abstract String destination(File file);

	protected synchronized List<File> reloadOutBox() {
		if (files.isEmpty()) {
			files = new ArrayList<>(Arrays.asList(Objects.requireNonNull(directory.listFiles(f -> f.getName().endsWith(extension())))));
			files.sort(Comparator.comparingLong(File::lastModified));
		}
		return this.files;
	}

	protected abstract String extension();
}