package io.intino.alexandria.messagehub;

import com.google.gson.Gson;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.message.Message;
import io.intino.alexandria.message.MessageReader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Instant;
import java.util.*;

class MessageOutBox {
	private final File directory;

	MessageOutBox(File directory) {
		this.directory = directory.getAbsoluteFile();
		if (!directory.exists()) directory.mkdirs();
	}

	void push(String channel, Message message) {
		try {
			Files.write(new File(directory, Instant.now().toString().replace(":", "_") + ".json").toPath(), new Gson().toJson(new SavedMessage(channel, message.toString())).getBytes());
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	Map.Entry<String, Message> get() {
		List<File> files = Arrays.asList(Objects.requireNonNull(directory.listFiles(f -> f.getName().endsWith(".json"))));
		Collections.sort(files);
		if (files.isEmpty()) return null;
		try {
			SavedMessage savedMessage = new Gson().fromJson(new String(Files.readAllBytes(files.get(0).toPath()), StandardCharsets.UTF_8), SavedMessage.class);
			return new AbstractMap.SimpleEntry<>(savedMessage.channel, new MessageReader(savedMessage.message).next());
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

	private static class SavedMessage {
		String channel;
		String message;

		SavedMessage(String channel, String message) {
			this.channel = channel;
			this.message = message;
		}
	}

}
