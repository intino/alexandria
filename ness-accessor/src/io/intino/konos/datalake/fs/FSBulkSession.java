package io.intino.konos.datalake.fs;

import io.intino.konos.datalake.EventDatalake;
import io.intino.ness.datalake.graph.Tank;
import io.intino.ness.inl.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

public class FSBulkSession implements EventDatalake.BulkSession {
	private static Logger logger = LoggerFactory.getLogger(FSBulkSession.class);
	private final List<Tank> tankList;

	private File directory;
	private Map<String, FileWriter> writters = new HashMap<>();

	public FSBulkSession(List<Tank> tankList) {
		this.tankList = tankList;
		try {
			directory = Files.createTempDirectory("bulk_session").toFile();
			for (Tank tank : tankList) {
				File file = fileOf(tank);
				file.createNewFile();
				this.writters.put(tank.qualifiedName(), new FileWriter(file));
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void append(String tank, List<Message> messages) {
		FileWriter writter = writters.get(tank);
		try {
			writter.write(map(messages.stream()));
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void append(String tank, Message... messages) {
		FileWriter writter = writters.get(tank);
		try {
			writter.write(map(stream(messages)));
			writter.flush();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private String map(Stream<Message> stream) {
		return stream.map(Message::toString).collect(Collectors.joining("\n\n"));
	}


	@Override
	public void finish() {
		for (Tank tank : tankList) {
			try {
				String bulk = new String(Files.readAllBytes(fileOf(tank).toPath()));
//				tank.putBulk(bulk);
			} catch (IOException e) {
			}
		}
	}

	private File fileOf(Tank tank) {
		return new File(directory, tank.qualifiedName() + ".inl");
	}
}
