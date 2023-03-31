package io.intino.alexandria.datalake.tests;

import io.intino.alexandria.FS;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.file.FileDatalake;
import io.intino.alexandria.event.message.MessageEvent;
import io.intino.alexandria.message.LegacyMessageReader;
import io.intino.alexandria.message.Message;
import io.intino.alexandria.message.MessageReader;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class ValidateDatalakeAfterMigration {

	private static final File oldDatalakeRoot = new File("temp/cm/old_datalake/events");
	private static final File newDatalakeRoot = new File("temp/cm/new_datalake");

	private final String tankName;
	private final File tank;

	public ValidateDatalakeAfterMigration(String tankName, File tank) {
		this.tankName = tankName;
		this.tank = tank;
	}

	@Test
	public void testTank() throws Exception {

		Datalake datalake = new FileDatalake(newDatalakeRoot);

		System.out.print(tank);

		assertTrue("Tank " + tank.getName() + " not found", datalake.messageStore().containsTank(tank.getName()));
		var newTank = datalake.messageStore().tank(tank.getName());

		for (File tub : FS.filesIn(tank, f -> f.getName().endsWith(".zim")).collect(Collectors.toList())) {
			testTub(newTank, tub);
		}

		System.out.println(" OK");
	}

	private static void testTub(Datalake.Store.Tank<MessageEvent> newTank, File oldTub) throws Exception {
		System.out.print("."); System.out.flush();

		try(LegacyMessageReader oldReader = new LegacyMessageReader(new GZIPInputStream(new FileInputStream(oldTub), 32 * 1024))) {
			Iterator<MessageEvent> newEvents = newTank.content((ss, ts) -> ts.equals(timetag(oldTub))).iterator();
			int i = 0;
			Instant lastTs = null;
			while(oldReader.hasNext()) {
				assertTrue("Missing events in timetag " + oldTub.getName() + ". Only " + i + " events found.", newEvents.hasNext());
				MessageEvent e1 = eventFrom(oldReader.next());
				MessageEvent e2 = newEvents.next();
				assertEquals("Events at " + i + " are different in " + oldTub.getName() + ":\n" + e1 + "\n\n" + e2, e1, e2);
				if(lastTs != null && lastTs.isAfter(e1.ts())) {
					File file = new File("temp/cm/errors/" + newTank.name() + ".tsv");
					file.getParentFile().mkdirs();
					Files.writeString(file.toPath(),
							timetag(oldTub) + "\t" + i + "\t" + lastTs + "\t" + e1.ts() + "\n",
							CREATE, APPEND);
				}
				lastTs = e1.ts();
				++i;
			}
		}
	}

	private static MessageEvent eventFrom(Message message) {
		if(!message.contains("ss")) message.set("ss", "default");
//		message.set("ss", normalize(message.get("ss").asString()));
		return new MessageEvent(message);
	}

	private static String normalize(String ss) {
		if(ss.length() > 20) return "default"; // Suspicious ss, lets set it to default
		return ss.replaceAll("[:]", "-");
	}

	private static String timetag(File zim) {
		return Timetag.of(zim.getName().replace(".zim", "")).value();
	}

	@Parameterized.Parameters(name = "{index}: {0}")
	public static Object[][] params() {
		return FS.directoriesIn(oldDatalakeRoot)
				.sorted(Comparator.comparing(FileUtils::sizeOfDirectory))
				.filter(dir -> FS.filesIn(dir, f -> true).findAny().isPresent())
//				.filter(dir -> dir.getName().equals("comercial.cuentamaestra.FacturacionServicioRechazada"))
				.map(f -> new Object[] {f.getName(), f}).toArray(Object[][]::new);
	}
}
