package io.intino.alexandria.nessaccesor;


import io.intino.alexandria.ness.setstore.session.SessionFileWriter;
import io.intino.alexandria.nessaccesor.NessAccessor.SetStore.Timetag;
import io.intino.alexandria.nessaccesor.NessAccessor.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.stream.Stream;

public class SetSession {

	private static Logger logger = LoggerFactory.getLogger(SetSession.class);
	private final SessionFileWriter writer;
	private int maxSize = 1000000;
	private int count = 0;

	SetSession(Stage stage) {
		this.writer = new SessionFileWriter(stage.start(Stage.Type.set));
	}

	public void autoFlush(int maxSize) {
		this.maxSize = maxSize;
	}

	public Chunk chunk(String tank, Timetag timetag, String set) {
		return new Chunk() {

			@Override
			public void put(Stream<Long> ids) {
				ids.forEach(i -> writer.add(tank, timetag.toString(), set, i));
				if (count++ >= maxSize) doFlush();
			}

			@Override
			public void put(long... ids) {
				for (long id : ids) writer.add(tank, timetag.toString(), set, id);
				if (count++ >= maxSize) doFlush();
			}
		};
	}

	private void doFlush() {
		try {
			flush();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void flush() throws IOException {
		writer.flush();
		count = 0;
	}

	public void close() throws IOException {
		writer.close();
	}

	public interface Chunk {

		void put(Stream<Long> ids);

		void put(long... ids);

	}

}
