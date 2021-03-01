package io.intino.alexandria.zim;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.message.Message;
import io.intino.alexandria.message.MessageWriter;
import org.xerial.snappy.SnappyOutputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@SuppressWarnings({"WeakerAccess"})
public class ZimBuilder {
	private final File source;

	public ZimBuilder(File file) {
		this.source = file;
		file.getParentFile().mkdirs();
	}

	public void put(Message... messages) {
		put(new ZimReader(messages));
	}

	public void put(List<Message> messages) {
		put(new ZimReader(messages));
	}

	public void put(Stream<Message> stream) {
		put(new ZimReader(stream));
	}

	public void put(ZimStream zimStream) {
		try {
			Files.move(merge(zimStream).toPath(), source.toPath(), REPLACE_EXISTING);
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private File merge(ZimStream data) {
		File file = tempFile();
		try (MessageWriter writer = new MessageWriter(zipStream(file))) {
			ZimStream stream = mergeFileWith(data);
			while (stream.hasNext()) writer.write(stream.next());
		} catch (IOException e) {
			Logger.error(e);
		}
		return file;
	}

	private SnappyOutputStream zipStream(File file) throws IOException {
		return new SnappyOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
	}


	private File tempFile() {
		try {
			return File.createTempFile("builder#", ".zim");
		} catch (IOException e) {
			Logger.error(e);
			return new File("builder#" + UUID.randomUUID().toString() + ".zim");
		}
	}

	private ZimStream mergeFileWith(ZimStream data) {
		return new ZimStream.Sequence(new ZimReader(source), data);
	}

}