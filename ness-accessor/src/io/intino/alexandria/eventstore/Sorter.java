package io.intino.alexandria.eventstore;

import io.intino.alexandria.Inl;
import io.intino.alexandria.eventstore.MessageExternalSorter;
import io.intino.alexandria.eventstore.graph.Tank;
import io.intino.ness.inl.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.intino.alexandria.eventstore.graph.Tank.INL;

public class Sorter {
	private static Logger logger = LoggerFactory.getLogger(io.intino.alexandria.eventstore.Sorter.class);
	static String SEPARATOR = "\n\n";

	private Tank tank;

	public Sorter(Tank tank) {
		this.tank = tank;
	}

	public void sort() {
		tank.flush();
		sort((Instant) null);
	}

	public void sort(Instant instant) {
		try {
			final List<File> inlFiles = Arrays.asList(Objects.requireNonNull(tank.directory().listFiles((f, n) -> n.endsWith(INL) && !isCurrentFile(n))));
			for (File file : instant == null ? inlFiles : inlFiles.stream().filter(f -> f.getName().equals(tank.fileFromInstant(instant) + INL)).sorted(Comparator.comparing(File::getName)).collect(Collectors.toList()))
				sort(file);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void sort(File file) throws IOException {
		logger.info("sorting " + tank.qualifiedName() + " " + file.getName());
		if (inMb(file.length()) > 30) new MessageExternalSorter(file).sort();
		else overwrite(file, new io.intino.alexandria.eventstore.TimSort<Message>().doSort(loadMessages(file).toArray(new Message[0]), messageComparator()));
	}

	private void overwrite(File file, Message[] messages) {
		try {
			file.delete();
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			for (Message message : messages) writer.write(message.toString() + SEPARATOR);
			writer.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private List<Message> loadMessages(File inlFile) throws IOException {
		return Inl.load(new String(readFile(inlFile), Charset.forName("UTF-8")));
	}

	private byte[] readFile(File inlFile) throws IOException {
		return Files.readAllBytes(inlFile.toPath());
	}

	private long inMb(long length) {
		return length / (1024 * 1024);
	}

	private Comparator<Message> messageComparator() {
		return Comparator.comparing(m -> {
			final String text = m.get("ts");
			if (text == null) logger.error("ts is null for message: " + m.toString());
			return Instant.parse(text);
		});
	}

	private boolean isCurrentFile(String file) {
		return (tank.fileFromInstant(Instant.now()) + INL).equals(file);
	}

}
