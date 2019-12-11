package io.intino.alexandria.event;

import com.google.gson.Gson;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.message.MessageReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.*;

class EventOutBox {
	private final File directory;

	EventOutBox(File directory) {
		this.directory = directory.getAbsoluteFile();
		if (!directory.exists()) directory.mkdirs();
	}

	void push(String channel, Event event) {
		try {
			Files.write(new File(directory, Instant.now().toString().replace(":", "_") + "_" + UUID.randomUUID().toString() + ".json").toPath(), new Gson().toJson(new SavedEvent(channel, event.toString())).getBytes());
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	Map.Entry<String, Event> get() {
		List<File> files = Arrays.asList(Objects.requireNonNull(directory.listFiles(f -> f.getName().endsWith(".json"))));
		Collections.sort(files);
		if (files.isEmpty()) return null;
		try {
			String json = Files.readString(files.get(0).toPath());
			if (json.isEmpty() || json.isBlank()) return null;
			SavedEvent savedEvent = new Gson().fromJson(json, SavedEvent.class);
			return new AbstractMap.SimpleEntry<>(savedEvent.channel, new Event(new MessageReader(savedEvent.message).next()));
		} catch (IOException e) {
			Logger.error(e);
			return null;
		}
	}

	void pop() {
		List<File> files = Arrays.asList(Objects.requireNonNull(directory.listFiles(f -> f.getName().endsWith(".json"))));
		Collections.sort(files);
		if (files.isEmpty()) return;
		files.get(0).delete();
	}

	boolean isEmpty() {
		File[] files = Objects.requireNonNull(directory.listFiles(f -> f.getName().endsWith(".json")));
		for (File file : files) if (file.length() == 0) file.delete();
		files = Objects.requireNonNull(directory.listFiles(f -> f.getName().endsWith(".json")));
		return files.length == 0;
	}

	private static class SavedEvent {
		String channel;
		String message;

		SavedEvent(String channel, String message) {
			this.channel = channel;
			this.message = message;
		}
	}

}
