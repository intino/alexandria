package io.intino.alexandria.zit;

import com.github.luben.zstd.ZstdInputStream;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.zit.model.Data;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

@SuppressWarnings({"all"})
public class ZitStream extends AbstractItzStream implements Iterator<Data>, AutoCloseable {
	public static ZitStream of(InputStream is) {
		return new ZitStream(readerOf(is instanceof ZstdInputStream ? is : ZitStream.zstdStreamOf(is)));
	}

	public static ZitStream of(ItsReader reader) {
		return new ZitStream(reader);
	}

	public static ZitStream of(File file) throws IOException {
		return new ZitStream(readerOf(inputStream(file)));
	}

	private final Iterator<Data> data;
	private final ItsReader reader;
	private final List<Runnable> closeHandlers;

	public ZitStream(ItsReader reader) {
		this.reader = requireNonNull(reader);
		this.closeHandlers = new LinkedList<>();
		this.data = reader.data().iterator();
	}

	@Override
	public Iterator<Data> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return data.hasNext();
	}

	@Override
	public Data next() {
		return data.next();
	}

	public String sensor() {
		return reader.sensor();
	}

	@Override
	public void forEach(Consumer<? super Data> action) {
		try {
			while (hasNext()) action.accept(next());
		} finally {
			close();
		}
	}

	@Override
	public Stream<Data> onClose(Runnable closeHandler) {
		if (closeHandler != null) this.closeHandlers.add(closeHandler);
		return this;
	}

	@Override
	public void close() {
		closeIterator(reader);
		closeHandlers.forEach(Runnable::run);
	}

	private static void closeIterator(ItsReader iterator) {
		if (iterator instanceof AutoCloseable)
			try {
				((AutoCloseable) iterator).close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
	}

	private static ItsReader readerOf(InputStream is) {
		return new ItsReader(is);
	}

	private static InputStream inputStream(File file) throws IOException {
		return zstdStreamOf(file);
	}

	private static InputStream zstdStreamOf(File file) throws IOException {
		return new ZstdInputStream(fileInputStream(file));
	}

	private static InputStream zstdStreamOf(InputStream stream) {
		try {
			return new ZstdInputStream(stream);
		} catch (IOException e) {
			Logger.error(e);
			return stream;
		}
	}

	private static BufferedInputStream fileInputStream(File file) throws FileNotFoundException {
		return new BufferedInputStream(new FileInputStream(file));
	}
}