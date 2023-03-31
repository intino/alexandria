package io.intino.alexandria.datalake.tests;

import io.intino.alexandria.FS;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.event.EventWriter;
import io.intino.alexandria.event.message.MessageEvent;
import io.intino.alexandria.event.message.MessageEventWriter;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.message.Message;
import io.intino.alexandria.message.MessageReader;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

public class MigrateEventsToNewDatalake {

	// TODO: hay algunos tipos de eventos, como MedicionFacturacion, que tienen como ss identificadores unicos, lo que multiplica
	// el numero de subdirectorios en el tanque de forma bestial. Hay que abordar este problema al migrarlo. Quizas el ss ponerlo como
	// default y el identificador almacenarlo como un atributo dentro del mensaje
	public static void main(String[] args) throws IOException {
		new MigrateEventsToNewDatalake().run();
	}

	private final AtomicInteger count = new AtomicInteger();

	public void run() throws IOException {
		File oldDatalakeRoot = new File("temp/cm/old_datalake/events");
		File newDatalakeRoot = new File("temp/cm/new_datalake/messages");
		reset(newDatalakeRoot);

		long start = System.currentTimeMillis();

		List<File> directories = FS.directoriesIn(oldDatalakeRoot).collect(Collectors.toList());
		int total = (int) FS.allFilesIn(oldDatalakeRoot, f -> f.getName().endsWith(".zim")).count();
		AtomicInteger tankCount = new AtomicInteger();

		directories.stream()
//				.parallel()
//				.filter(tank -> tank.getName().equals("comercial.cuentamaestra.FacturacionServicioRechazada"))
				.sorted(Comparator.comparing(FileUtils::sizeOfDirectory))
				.forEach(tank -> {
					try {
						var zims = FS.allFilesIn(tank, f -> f.getName().endsWith(".zim")).collect(Collectors.toList());
						System.out.println("Tank: " + tank.getName() + " (" + tankCount.incrementAndGet() + "/" + directories.size() + ") with " + zims.size() + " .zim, " + (FileUtils.sizeOfDirectory(tank)/1024.0f/1024.0f) + " MB");
						Map<File, EventWriter<MessageEvent>> writers = new ConcurrentHashMap<>();
						zims.forEach(zim -> migrate(zim, newDatalakeRoot, total));
						close(writers);
					} catch (IOException e) {
						Logger.error(e);
					}
				});

		System.out.println("Complete (" + count.get() + " / " + total + "): " + ((System.currentTimeMillis() - start) / 1000.0f) + " seconds");
	}

	private static void reset(File newDatalakeRoot) throws IOException {
		if(newDatalakeRoot.exists()) {
			File dest = new File(newDatalakeRoot.getAbsolutePath() + System.currentTimeMillis());
			if(newDatalakeRoot.renameTo(dest))
				new Thread(() -> {
					try {
						FileUtils.deleteDirectory(dest);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}).start();
			else
				FileUtils.deleteDirectory(newDatalakeRoot);
		}
		newDatalakeRoot.mkdirs();
	}

	private void close(Map<File, EventWriter<MessageEvent>> writers) {
		for(EventWriter<MessageEvent> writer : writers.values()) {
			try {
				writer.close();
			} catch (Exception e) {
				Logger.error(e);
			}
		}
		writers.clear();
	}

	private void migrate(File zim, File newDatalakeRoot, int total) {
		try(MessageReader reader = new MessageReader(open(zim))) {

			String tankName = zim.getParentFile().getName();
			Map<String, List<MessageEvent>> events = new HashMap<>();

			while(reader.hasNext()) {
				Message message = reader.next();
				if(!message.contains("ss")) message.set("ss", "default");
//				message.set("ss", normalize(message.get("ss").asString()));
				String ss = normalize(message.get("ss").asString());
				MessageEvent event = new MessageEvent(message);
				events.computeIfAbsent(ss, k -> new ArrayList<>()).add(event);
			}

			for(var entry : events.entrySet()) {
				File file = new File(newDatalakeRoot, tankName + "/" + entry.getKey() + "/" + timetag(zim) + ".zim");
				file.getParentFile().mkdirs();
				try(MessageEventWriter writer = new MessageEventWriter(file, true)) {
					writer.write(entry.getValue());
				}
				count.incrementAndGet();
			}

//			int c = count.incrementAndGet();
//			if(c % 100 == 0) System.out.println(">> " + String.format("%.02f%%", (c / (float)total) * 100));
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	private String normalize(String ss) {
		if(ss.length() > 20) return "default"; // Suspicious ss, lets set it to default
		return ss.replaceAll("[:]", "-");
	}

	private String timetag(File zim) {
		return Timetag.of(zim.getName().replace(".zim", "")).value();
	}

	private InputStream open(File zim) throws IOException {
		return new GZIPInputStream(new BufferedInputStream(new FileInputStream(zim)));
	}
}
