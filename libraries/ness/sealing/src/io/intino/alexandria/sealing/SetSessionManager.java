package io.intino.alexandria.sealing;

import io.intino.alexandria.Fingerprint;
import io.intino.alexandria.Session;
import io.intino.alexandria.datalake.file.FS;
import io.intino.alexandria.datalake.file.FileSetStore;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.triplestore.FileTripleStore;
import io.intino.alexandria.triplestore.MemoryTripleStore;
import io.intino.alexandria.triplestore.TripleStore;
import io.intino.alexandria.zet.ZetReader;
import io.intino.alexandria.zet.ZetStream;
import io.intino.alexandria.zet.ZetWriter;

import java.io.*;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.intino.alexandria.Session.SessionExtension;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

class SetSessionManager {
	private static final DecimalFormat FORMATTER = new DecimalFormat("#.##");
	private final List<File> files;
	private final File setStoreFolder;
	private final File tempFolder;
	private int count = 1;

	private SetSessionManager(List<File> files, File setStoreFolder, File tempFolder) {
		this.files = files;
		this.setStoreFolder = setStoreFolder;
		this.tempFolder = tempFolder;
	}

	static void seal(File stageFolder, File setStoreFolder, File tempFolder) {
		new SetSessionManager(sessionsOf(stageFolder), setStoreFolder, tempFolder).seal();
	}

	private static List<File> sessionsOf(File stageFolder) {
		return FS.allFilesIn(stageFolder, f -> f.getName().endsWith(SessionExtension)).collect(Collectors.toList());
	}

	private static File fileFor(Session session, File stageFolder) {
		return new File(stageFolder, filename(session));
	}

	private static String filename(Session session) {
		return session.name() + SessionExtension;
	}


	private static String extensionOf(Session.Type type) {
		return "." + type.name() + SessionExtension;
	}

	private void seal() {
		sealSetSessions();
		sealSetMetadataSessions();
	}

	private void sealSetMetadataSessions() {
		Map<File, FileTripleStore.Builder> map = new HashMap<>();
		try {
			loadSetMetadataSessions().forEach((key, value) -> {
				String[] triple = value.all().flatMap(Arrays::stream).toArray(String[]::new);
				if (triple.length > 0) processTriple(triple, map);
				markTreated(key);
			});
		} catch (Throwable e) {
			Logger.error(e);
		}
		map.values().forEach(FileTripleStore.Builder::close);
	}

	private void processTriple(String[] triple, Map<File, TripleStore.Builder> map) {
		Fingerprint fingerprint = new Fingerprint(triple[0]);
		tripleStoreFor(metadataFileOf(fingerprint), map).put(fingerprint.set(), triple[1], triple[2]);
	}

	private File metadataFileOf(Fingerprint fingerprint) {
		File file = new File(setStoreFolder, fingerprint.tank() + "/" + fingerprint.timetag() + "/" + FileSetStore.MetadataFilename);
		file.getParentFile().mkdirs();
		return file;
	}

	private TripleStore.Builder tripleStoreFor(File file, Map<File, TripleStore.Builder> map) {
		if (!map.containsKey(file)) {
			try {
				map.put(file, new FileTripleStore.Builder(new FileOutputStream(file, true)));
			} catch (FileNotFoundException e) {
				Logger.error(e);
			}
		}
		return map.get(file);
	}

	private Map<File, MemoryTripleStore> loadSetMetadataSessions() {
		return files.parallelStream()
				.filter(f -> f.getName().endsWith(extensionOf(Session.Type.setMetadata)))
				.collect(toMap(f -> f, f -> new MemoryTripleStore(inputStreamOf(f)), (a, b) -> b));
	}

	private InputStream inputStreamOf(File file) {
		try {
			return new BufferedInputStream(new FileInputStream(file));
		} catch (IOException e) {
			Logger.error(e);
			return null;
		}
	}

	private void sealSetSessions() {
		List<SetSessionFileReader> readers = setSessionReaders();
		int size = readers.stream().mapToInt(SetSessionFileReader::size).sum();
		readers.forEach(r -> {
			sealFingerprints(readers, size, r);
			markTreated(r.file());
		});
	}

	private void markTreated(File file) {
		file.renameTo(new File(file.getAbsolutePath() + ".treated"));
	}

	private void sealFingerprints(List<SetSessionFileReader> readers, int size, SetSessionFileReader r) {
		r.fingerprints().parallelStream().forEach(fp -> {
			if (count % 10000 == 0) Logger.info(FORMATTER.format((count * 100.) / size) + "%");
			seal(fp, readers);
			deleteIndex(fp);
			count++;
		});
	}

	private void deleteIndex(Fingerprint fp) {
		File file = new File(fileOf(fp).getParent(), FileSetStore.IndexFileName);
		if (file.exists()) file.delete();
	}

	private List<SetSessionFileReader> setSessionReaders() {
		return setSessions().map(this::setSessionReader).collect(toList());
	}

	private SetSessionFileReader setSessionReader(File file) {
		try {
			return new SetSessionFileReader(file);
		} catch (IOException e) {
			Logger.error(e);
			return null;
		}
	}

	private void seal(Fingerprint fingerprint, List<SetSessionFileReader> readers) {
		try {
			File setFile = fileOf(fingerprint);
			File tempFile = merge(fingerprint, readers);
			Files.move(tempFile.toPath(), setFile.toPath(), REPLACE_EXISTING);
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private File merge(Fingerprint fingerprint, List<SetSessionFileReader> readers) throws IOException {
		File tempFile = File.createTempFile(fingerprint.toString(), FileSetStore.SetExtension, tempFolder);
		List<ZetStream> streams = zetStreamsOf(fingerprint, readers);
		new ZetWriter(tempFile).write(streams.size() == 1 ? streams.get(0) : new ZetStream.Merge(streams));
		return tempFile;
	}

	private List<ZetStream> zetStreamsOf(Fingerprint fingerprint, List<SetSessionFileReader> readers) {
		List<ZetStream> streams = collectZetStreams(fingerprint, readers);
		File setFile = fileOf(fingerprint);
		if (setFile.exists()) streams.add(new ZetReader(setFile));
		return streams;
	}


	private List<ZetStream> collectZetStreams(Fingerprint fingerprint, List<SetSessionFileReader> readers) {
		List<ZetStream> list = new ArrayList<>();
		for (SetSessionFileReader reader : readers) list.addAll(reader.streamsOf(fingerprint));
		return list;
	}

	private Stream<File> setSessions() {
		return files.stream().filter(f -> f.getName().endsWith(extensionOf(Session.Type.set)));
	}

	private File fileOf(Fingerprint fingerprint) {
		File file = new File(setStoreFolder, fingerprint + FileSetStore.SetExtension);
		file.getParentFile().mkdirs();
		return file;
	}

	private Set<Fingerprint> fingerPrintsIn(List<SetSessionFileReader> readers) {
		Set<Fingerprint> set = new HashSet<>();
		for (SetSessionFileReader reader : readers) set.addAll(reader.fingerprints());
		return set;
	}

}