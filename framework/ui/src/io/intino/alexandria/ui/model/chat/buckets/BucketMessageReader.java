package io.intino.alexandria.ui.model.chat.buckets;

import io.intino.alexandria.Json;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.ui.model.chat.Message;
import io.intino.alexandria.ui.model.chat.MessageReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class BucketMessageReader implements MessageReader {
	private final List<File> sources;
	private List<String> sourceLines;
	private int sourceIndex;
	private int contentIndex;

	public BucketMessageReader(File root) {
		sources = loadSources(root);
		sourceLines = Collections.emptyList();
		sourceIndex = sources.size();
		contentIndex = -1;
	}

	@Override
	public Iterator<Message> iterator() {
		return new Iterator<>() {
			@Override
			public boolean hasNext() {
				return (sourceIndex > 0 && !sources.isEmpty()) || contentIndex > 0;
			}

			@Override
			public Message next() {
				if (!hasNext()) return null;
				while (contentIndex <= 0 && sourceIndex > 0) nextSource();
				contentIndex--;
				return contentIndex >= 0 ? messageFrom(sourceLines.get(contentIndex)) : null;
			}
		};
	}

	private void nextSource() {
		sourceIndex--;
		sourceLines = readSourceLines();
		contentIndex = sourceLines.size();
	}

	private List<String> readSourceLines() {
		try {
			return Files.readAllLines(sources.get(sourceIndex).toPath());
		} catch (IOException e) {
			Logger.error(e);
			return Collections.emptyList();
		}
	}

	private List<File> loadSources(File root) {
		File[] files = root.listFiles();
		return files != null ? Arrays.stream(files).filter(f -> !f.isDirectory()).sorted((o1, o2) -> Timetag.of(o2.getName()).compare(Timetag.of(o1.getName()))).collect(toList()) : Collections.emptyList();
	}

	private Message messageFrom(String line) {
		return Json.fromJson(line, Message.class);
	}

}
