package io.intino.alexandria.event.message;

import io.intino.alexandria.event.AbstractEventWriter;
import io.intino.alexandria.zim.ZimWriter;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

public class MessageEventWriter extends AbstractEventWriter<MessageEvent> {

	public MessageEventWriter(File file) {
		super(file);
	}

	@Override
	protected File merge(Stream<MessageEvent> data) throws IOException {
		File file = tempFile();
		try (ZimWriter writer = new ZimWriter(file)) {
			try(Stream<MessageEvent> merged = mergeFileWith(data)) {
				Iterator<MessageEvent> events = merged.iterator();
				while (events.hasNext()) writer.write(events.next().toMessage());
			}
		}
		return file;
	}
}