package io.intino.alexandria.ztp;

import io.intino.alexandria.logger.Logger;

import java.io.*;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Stream;

import static io.intino.alexandria.ztp.Ztp.ZTP_EXTENSION;

public class ZtpBuilder implements AutoCloseable {

	private final File destination;
	private final File tempFile;
	private final TupleWriter writer;

	public ZtpBuilder(File destination) throws IOException {
		this.destination = destination;
		this.tempFile = tempFile();
		this.writer = new TupleWriter(open(tempFile));
	}

	public void put(Tuple... tuples) throws IOException {
		writer.write(tuples);
	}

	public void put(Collection<Tuple> tuples) throws IOException {
		writer.write(tuples);
	}

	public void put(Stream<Tuple> tuples) throws IOException {
		writer.write(tuples);
	}

	public void put(Tuple tuple) throws IOException {
		writer.write(tuple);
	}

	@Override
	public void close() throws IOException {
		try {
			writer.close();
			appendTempFileToDestination();
		} finally {
			tempFile.delete();
		}
	}

	private void appendTempFileToDestination() throws IOException {
		destination.getParentFile().mkdirs();
		try(OutputStream out = Ztp.compressing(new BufferedOutputStream(new FileOutputStream(destination, true)))) {
			try(InputStream in = new BufferedInputStream(new FileInputStream(tempFile))) {
				byte[] buffer = new byte[1024];
				int read;
				while((read = in.read(buffer)) > 0) {
					out.write(buffer, 0, read);
				}
			}
		}
	}

	private OutputStream open(File tmpFile) throws FileNotFoundException {
		return new BufferedOutputStream(new FileOutputStream(tmpFile));
	}

	private File tempFile() {
		try {
			return File.createTempFile("ztpbuilder#", ZTP_EXTENSION);
		} catch (IOException e) {
			Logger.error(e);
			return new File("ztpbuilder#" + UUID.randomUUID() + ZTP_EXTENSION);
		}
	}
}
