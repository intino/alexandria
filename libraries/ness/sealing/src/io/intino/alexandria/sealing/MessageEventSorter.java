package io.intino.alexandria.sealing;

import com.github.luben.zstd.ZstdInputStream;
import com.github.luben.zstd.ZstdOutputStream;
import io.intino.alexandria.logger.Logger;

import java.io.*;
import java.nio.file.Files;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.time.Instant.parse;

public class MessageEventSorter {
	private final File file;
	private final File temp;
	private final List<Tuple> tuples;

	public MessageEventSorter(File file, File tempFolder) throws IOException {
		this.file = file;
		this.temp = File.createTempFile("event", ".inl", tempFolder);
		this.tuples = new ArrayList<>();
	}

	void sort() throws IOException {
		sort(file);
	}

	void sort(File destination) throws IOException {
		try {
			read();
			tuples.sort(Comparator.comparing(t -> t.ts));
			write(outputStream(destination));
			Files.delete(temp.toPath());
		} catch (IOException io) {
			if (temp.exists()) {
				Logger.warn("Deleting inl temporal file " + file.getAbsolutePath());
				Files.delete(temp.toPath());
			}
			throw io;
		}
	}

	private void read() throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream())); BufferedWriter writer = new BufferedWriter(new FileWriter(temp))) {
			int offset = 0;
			int size = 0;
			Instant instant = null;
			while (true) {
				String line = reader.readLine();
				if (line == null) break;
				if (isTS(line)) instant = parse(line.substring(line.indexOf(":") + 1).trim());
				else if (isMainHeader(line)) {
					addTuple(instant, offset, size);
					offset += size;
					size = 0;
				}
				size += line.getBytes().length + 1;
				writer.write(line + "\n");
			}
			addTuple(instant, offset, size);
		}
	}

	private void write(OutputStream output) throws IOException {
		try (RandomAccessFile input = new RandomAccessFile(temp, "r")) {
			for (Tuple t : tuples) write(output, bytesOf(input, t));
			output.close();
		}
	}

	private void write(OutputStream output, byte[] bytes) {
		try {
			output.write(bytes);
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private OutputStream outputStream(File file) throws IOException {
		return new BufferedOutputStream(new ZstdOutputStream(new FileOutputStream(file)));
	}

	private byte[] bytesOf(RandomAccessFile accessFile, Tuple tuple) {
		try {
			return read(accessFile, (int) (tuple.next >> 32), new byte[(int) tuple.next]);
		} catch (IOException e) {
			Logger.error(e);
			return new byte[0];
		}
	}

	private byte[] read(RandomAccessFile accessFile, int offset, byte[] buffer) throws IOException {
		accessFile.seek(offset);
		accessFile.read(buffer);
		return buffer;
	}

	private void addTuple(Instant instant, int offset, int size) {
		if (instant == null) return;
		tuples.add(new Tuple(instant, (((long) offset) << 32) + ((long) size)));
	}

	private boolean isTS(String line) {
		return line.startsWith("ts:");
	}

	private boolean isMainHeader(String line) {
		return line.startsWith("[") && !line.contains(".");
	}

	private InputStream inputStream() throws IOException {
		return new ZstdInputStream(new FileInputStream(file));
	}

	private static class Tuple {
		Instant ts;
		long next;

		public Tuple(Instant ts, long next) {
			this.ts = ts;
			this.next = next;
		}
	}
}
