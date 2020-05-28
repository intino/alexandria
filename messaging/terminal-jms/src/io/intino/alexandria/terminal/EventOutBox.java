package io.intino.alexandria.terminal;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.event.Event;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.message.MessageReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.UUID;

class EventOutBox extends OutBox {
	private static final String INL = ".inl";

	EventOutBox(File directory) {
		super(directory);
	}

	Map.Entry<String, Event> get() {
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
			return new AbstractMap.SimpleEntry<>(destination(file), new Event(new MessageReader(content).next()));
		} catch (IOException e) {
			Logger.error(e);
			return null;
		}
	}

	void push(String channel, Event event) {
		try {
			Files.write(new File(directory, channel + "#" + timetag(event) + "#" + UUID.randomUUID().toString() + INL).toPath(), event.toString().getBytes());
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private String timetag(Event event) {
		return new Timetag(LocalDateTime.ofInstant(event.ts(), ZoneOffset.UTC), Scale.Minute).toString();
	}

	protected String destination(File file) {
		return file.getName().substring(0, file.getName().indexOf("#"));
	}

	@Override
	protected String extension() {
		return INL;
	}
}