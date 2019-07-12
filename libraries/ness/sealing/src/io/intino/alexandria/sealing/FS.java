package io.intino.alexandria.sealing;

import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class FS {
	private File root;

	public FS(File root) {
		this.root = root;
	}

	public static Stream<File> foldersIn(File folder) {
		return Arrays.stream(new FS(folder).filesIn(File::isDirectory, Sort.Normal));
	}

	public static Stream<File> foldersIn(File folder, Sort sort) {
		return Arrays.stream(new FS(folder).filesIn(File::isDirectory, sort));
	}

	public static Stream<File> filesIn(File folder, FileFilter filter) {
		return Arrays.stream(new FS(folder).filesIn(filter, Sort.Normal));
	}

	public static void copyInto(File destination, InputStream inputStream) {
		try {
			Files.copy(inputStream, destination.toPath());
			inputStream.close();
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	public static Stream<File> allFilesIn(File directory, FileFilter filter) {
		return allFilesIn(directory.toPath(), filter).stream();
	}

	private static List<File> allFilesIn(Path path, FileFilter filter) {
		List<File> files = new ArrayList<>();
		try (Stream<Path> paths = Files.walk(path)) {
			paths.filter(p -> Files.isRegularFile(p) && filter.accept(p.toFile())).forEach(p -> files.add(p.toFile()));
		} catch (IOException e) {
			Logger.error(e);
		}
		return files;
	}

	private File[] filesIn(FileFilter filter, Sort sort) {
		File[] files = root.listFiles(filter);
		files = files == null ? new File[0] : files;
		Arrays.sort(files, sort.comparator);
		return files;
	}


	public enum Sort {
		Normal((x, y) -> x.getName().compareTo(y.getName())),
		Reversed((x, y) -> y.getName().compareTo(x.getName()));

		private final Comparator<File> comparator;

		Sort(Comparator<File> comparator) {
			this.comparator = comparator;
		}
	}
}
