package io.intino.alexandria.ui.model.chat.buckets;

import io.intino.alexandria.Json;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.ui.model.chat.Message;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class BucketMessageWriter {
	private final File file;
	private final List<String> messages;

	public BucketMessageWriter(File file) {
		this.file = file;
		this.messages = load();
	}

	public void add(Message message) {
		messages.add(Json.toString(message));
	}

	public void save() {
		try {
			file.getParentFile().mkdirs();
			Files.writeString(file.toPath(), String.join("\n", messages));
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private List<String> load() {
		try {
			if (!file.exists()) return new ArrayList<>();
			return Files.readAllLines(file.toPath());
		} catch (IOException e) {
			Logger.error(e);
			return new ArrayList<>();
		}
	}

}
