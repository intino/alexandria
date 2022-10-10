package io.intino.alexandria.datalake.file;

import io.intino.alexandria.datalake.Datalake;

import java.io.File;
import java.util.stream.Stream;

public class FileTripletStore implements Datalake.TripletStore {
	public static final String Extension = ".tsv";
	private final File root;

	public FileTripletStore(File root) {
		this.root = root;
		this.root.mkdirs();
	}

	@Override
	public Stream<Tank> tanks() {
		return FS.foldersIn(root).map(FileTripletTank::new);
	}

	@Override
	public FileTripletTank tank(String name) {
		return new FileTripletTank(new File(root, name));
	}
}
