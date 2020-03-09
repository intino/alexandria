package io.intino.alexandria.event;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.message.MessageReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

class EventOutBox {
	private static final String INL = ".inl";
	private final File directory;
	private List<File> files;

	EventOutBox(File directory) {
		this.directory = directory.getAbsoluteFile();
		this.files = new ArrayList<>();
		if (!directory.exists()) directory.mkdirs();
		else reloadOutBox();
	}

	void push(String channel, Event event) {
		try {
			Files.write(new File(directory, channel + "#" + timetag(event) + "#" + UUID.randomUUID().toString() + INL).toPath(), event.toString().getBytes());
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	Map.Entry<String, Event> get() {
		files.sort(Comparator.comparingLong(File::lastModified));
		if (files.isEmpty()) return null;
		try {
			File file = files.get(0);
			String inl = Files.readString(file.toPath());
			if (inl.isEmpty() || inl.isBlank()) return null;
			return new AbstractMap.SimpleEntry<>(tank(file), new Event(new MessageReader(inl).next()));
		} catch (IOException e) {
			Logger.error(e);
			return null;
		}
	}

	void pop() {
		reloadOutBox();
		if (files.isEmpty()) return;
		files.get(0).delete();
		files.remove(0);
	}

	private String timetag(Event event) {
		return new Timetag(LocalDateTime.ofInstant(event.ts(), ZoneOffset.UTC), Scale.Minute).toString();
	}

	private String tank(File file) {
		return file.getName().substring(0, file.getName().indexOf("#"));
	}

	boolean isEmpty() {
		return reloadOutBox().isEmpty();
	}

	private synchronized List<File> reloadOutBox() {
		if (files.isEmpty()) {
			files = new ArrayList<>(Arrays.asList(Objects.requireNonNull(directory.listFiles(f -> f.getName().endsWith(INL)))));
			files.sort(Comparator.comparingLong(File::lastModified));
		}
		return this.files;
	}

}
