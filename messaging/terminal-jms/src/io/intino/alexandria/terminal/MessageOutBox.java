package io.intino.alexandria.terminal;

import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.UUID;

class MessageOutBox extends OutBox {
	private static final String JSON = ".json";

	MessageOutBox(File directory) {
		super(directory);
	}

	Map.Entry<String, String> get() {
		files.sort(Comparator.comparingLong(File::lastModified));
		if (files.isEmpty()) return null;
		try {
			File file = files.get(0);
			if (!file.exists()) {
				files.remove(file);
				return null;
			}
			String content = Files.readString(file.toPath());
			if (content.isEmpty() || content.isBlank()) return null;
			return new AbstractMap.SimpleEntry<>(destination(file), content);
		} catch (IOException e) {
			Logger.error(e);
			return null;
		}
	}

	@Override
	protected String extension() {
		return JSON;
	}

	@Override
	protected String destination(File file) {
		return file.getName().substring(0, file.getName().indexOf("#"));
	}

	void push(String path, String message) {
		try {
			Files.write(new File(directory, path + "#" + UUID.randomUUID().toString() + JSON).toPath(), message.getBytes());
		} catch (IOException e) {
			Logger.error(e);
		}
	}


}
