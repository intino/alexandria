package io.intino.alexandria;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

public class FS {

	public static Stream<File> directoriesIn(File directory) {
		return Arrays.stream(filesIn(directory, File::isDirectory, Sort.Natural));
	}

	public static Stream<File> directoriesIn(File directory, Sort sort) {
		return Arrays.stream(filesIn(directory, File::isDirectory, sort));
	}

	public static Stream<File> filesIn(File directory, FileFilter filter) {
		return Arrays.stream(filesIn(directory, filter, Sort.Natural));
	}

	public static Stream<File> allFilesIn(File directory, FileFilter filter) throws IOException {
		return allFilesIn(directory.toPath(), filter);
	}

	public static void copyInto(File destination, InputStream inputStream) throws IOException {
		Files.copy(inputStream, destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}

	public static void append(File destination, InputStream inputStream) throws IOException {
		try(OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destination, true))) {
			inputStream.transferTo(outputStream);
		}
	}

	private static Stream<File> allFilesIn(Path path, FileFilter filter) throws IOException {
		Stream.Builder<File> streamBuilder = Stream.builder();
		try (Stream<Path> paths = Files.walk(path)) {
			paths.filter(p -> Files.isRegularFile(p) && filter.accept(p.toFile())).forEach(p -> streamBuilder.add(p.toFile()));
		}
		return streamBuilder.build();
	}

	private static File[] filesIn(File root, FileFilter filter, Sort sort) {
		File[] files = root.listFiles(filter);
		files = files == null ? new File[0] : files;
		Arrays.sort(files, sort.comparator);
		return files;
	}

	public enum Sort {
		Natural(Comparator.comparing(File::getName)),
		Reversed(Comparator.comparing(File::getName).reversed());

		private final Comparator<File> comparator;

		Sort(Comparator<File> comparator) {
			this.comparator = comparator;
		}
	}
}
