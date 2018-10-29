package io.intino.konos.datalake.fs;

import io.intino.konos.datalake.EventDatalake;
import io.intino.ness.datalake.Scale;
import io.intino.ness.datalake.graph.Tank;
import io.intino.ness.inl.Message;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.intino.ness.datalake.graph.Tank.fileFromInstant;

public class FSEventSession implements EventDatalake.EventSession {
	public static final String INL = ".inl";
	private static Logger logger = LoggerFactory.getLogger(FSEventSession.class);
	private final List<Tank> tankList;
	private final Map<String, TankWriter> writers = new HashMap<>();
	private File directory;

	public FSEventSession(List<Tank> tankList, Scale scale) {
		this.tankList = tankList;
		try {
			directory = Files.createTempDirectory("bulk_session").toFile();
			for (Tank tank : tankList) writers.put(tank.qualifiedName(), new TankWriter(tank.qualifiedName(), scale, directory));
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void append(String tank, List<Message> messages) {
		TankWriter writer = writers.get(tank);
		for (Message message : messages) writer.write(message);
		writer.flush();
	}

	@Override
	public void append(String tank, Message... messages) {
		TankWriter writer = writers.get(tank);
		for (Message message : messages) writer.write(message);
		writer.flush();
	}

	@Override
	public void close() {
		for (Tank tank : tankList) {
			tank.putBulk(loadBulk(tank));
		}
	}

	private Map<String, String> loadBulk(Tank tank) {
		TankWriter writer = writers.get(tank.qualifiedName());
		File[] files = writer.directory.listFiles(f -> f.getName().endsWith(INL));
		if (files == null) return Collections.emptyMap();
		Map<String, String> map = new HashMap<>();
		for (File file : files) {
			String messages = read(file);
			map.put(file.getName().replace(INL, ""), messages);
		}
		return map;
	}

	private String read(File file) {
		try {
			return new String(Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	private File fileOf(Tank tank) {
		return new File(directory, tank.qualifiedName() + INL);
	}

	private class TankWriter {
		final File directory;
		final String tank;
		final Scale scale;
		final Map<String, FileWriterWithEncoding> timeWriters = new HashMap<>();

		public TankWriter(String tank, Scale scale, File directory) {
			this.tank = tank;
			this.scale = scale;
			this.directory = new File(directory, tank);
			directory.mkdirs();
		}

		public void write(Message message) {
			String ts = fileFromInstant(message.get("ts"), scale);
			try {
				if (!timeWriters.containsKey(ts)) timeWriters.put(ts, new FileWriterWithEncoding(new File(directory, ts + INL), "UTF8"));
				timeWriters.get(ts).write(message.toString() + "\n\n");
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}

		public void flush() {
			try {
				for (FileWriterWithEncoding writer : timeWriters.values()) {
					writer.flush();
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}
