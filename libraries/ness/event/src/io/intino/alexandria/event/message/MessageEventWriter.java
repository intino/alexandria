package io.intino.alexandria.event.message;

import com.github.luben.zstd.ZstdOutputStream;
import io.intino.alexandria.event.AbstractEventWriter;
import io.intino.alexandria.message.MessageWriter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
		try (MessageWriter writer = new MessageWriter(zstd(file))) {
			Iterator<MessageEvent> events = mergeFileWith(data).iterator();
			while (events.hasNext()) writer.write(events.next().toMessage());
		}
		return file;
	}

	private ZstdOutputStream zstd(File file) throws IOException {
		return new ZstdOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
	}
}