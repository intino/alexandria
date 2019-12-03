package io.intino.alexandria.messagehub;

import com.google.gson.Gson;
import io.intino.alexandria.event.Event;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.message.MessageReader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
			Files.write(new File(directory, Instant.now().toString().replace(":", "_") + ".json").toPath(), new Gson().toJson(new SavedEvent(channel, event.toString())).getBytes());
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	Map.Entry<String, Event> get() {
		List<File> files = Arrays.asList(Objects.requireNonNull(directory.listFiles(f -> f.getName().endsWith(".json"))));
		Collections.sort(files);
		if (files.isEmpty()) return null;
		try {
			SavedEvent savedEvent = new Gson().fromJson(new String(Files.readAllBytes(files.get(0).toPath()), StandardCharsets.UTF_8), SavedEvent.class);
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
		return Objects.requireNonNull(directory.listFiles(f -> f.getName().endsWith(".json"))).length == 0;
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
