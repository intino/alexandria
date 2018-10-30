package io.intino.alexandria.ness.setstore.session;

import io.intino.alexandria.ness.setstore.file.FSSetStore;
import io.intino.sezzet.operators.FileReader;
import io.intino.sezzet.operators.SetStream;
import io.intino.sezzet.operators.Union;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static io.intino.alexandria.ness.setstore.file.FSSetStore.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class SessionSealer {
	private final List<File> files;
	private File storeFolder;

	private SessionSealer(List<File> files, File storeFolder) {
		this.files = files;
		this.storeFolder = storeFolder;
	}

	public static void seal(File stageFolder) {
		File[] sessions = stageFolder.listFiles(f -> f.getName().endsWith(SessionExt));
		if (sessions == null) return;
		new SessionSealer(asList(sessions), stageFolder.getParentFile()).seal();
		markAsProcessed(stageFolder, asList(sessions));
	}

	private static void markAsProcessed(File stageFolder, List<File> files) {
		String instant = Instant.now().toString().substring(0, 19).replace(":", "").replace("-", "");
		File processedFolder = new File(stageFolder, "processed/" + instant);
		processedFolder.mkdirs();
		files.forEach(f -> {
			try {
				Files.move(f.toPath(), new File(processedFolder, f.getName()).toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	private void seal() {
		List<SessionFileReader> readers = loadReaders();
		Set<String> distinctChunks = distinctChunks(readers);
		Logger.getGlobal().info("Sets to seal " + distinctChunks.size());
		distinctChunks.parallelStream().forEach(distinctChunk -> {
			try {
				List<SetStream> streams = chunksOf(readers, distinctChunk);
				File setFile = filepath(distinctChunk);
				File tempFile = new File(filepath(distinctChunk) + TempExt);
				if (setFile.exists()) streams.add(new FileReader(setFile));
				FSSetStore.write(new Union(streams), tempFile);
				Files.move(tempFile.toPath(), setFile.toPath(), REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	private List<SessionFileReader> loadReaders() {
		return files.parallelStream().map(f -> {
			try {
				return new SessionFileReader(f);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}).collect(toList());
	}

	private File filepath(String distinctChunk) {
		File output = new File(storeFolder, distinctChunk.replace("@", "/") + SetExt);
		output.getParentFile().mkdirs();
		return output;
	}

	private List<SetStream> chunksOf(List<SessionFileReader> readers, String distinctChunk) {
		return readers.stream()
				.map(r -> r.chunks(distinctChunk))
				.flatMap(Collection::stream)
				.map(SessionFileReader.Chunk::stream)
				.collect(toList());
	}

	private Set<String> distinctChunks(List<SessionFileReader> readers) {
		return readers.stream()
				.map(SessionFileReader::chunks)
				.flatMap(Collection::stream)
				.map(SessionFileReader.Chunk::id)
				.collect(Collectors.toSet());
	}

}
