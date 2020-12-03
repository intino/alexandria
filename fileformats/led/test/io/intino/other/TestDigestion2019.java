package io.intino.other;

import io.intino.alexandria.led.*;
import io.intino.alexandria.led.util.LedUtils;
import io.intino.alexandria.led.util.memory.MemoryUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class TestDigestion2019 {

	private static final File UNSORTED_SESSION_FILES_DIR = new File("G:\\leds");
	private static final int NUM_TRANSACTIONS_IN_MEMORY = 500_000;

	static {
		//MemoryUtils.USE_MEMORY_TRACKER.set(true);
	}

	@Parameterized.Parameters
	public static Collection<File> getParameters() {
		File[] files = UNSORTED_SESSION_FILES_DIR.listFiles();
		if(files == null || files.length == 0) {
			return Collections.emptyList();
		}
		System.out.println(">> Loading " + files.length + " files...");
		return Arrays.stream(files).filter(file -> {
			if(file.isDirectory() || file.getName().startsWith("sorted_")) {
				file.delete();
				file.deleteOnExit();
				return false;
			}
			return true;
		}).collect(Collectors.toUnmodifiableList());
	}

	private final File sessionUnsortedLedFile;
	private final File destinationSortedLedFile;

	public TestDigestion2019(File sessionUnsortedLedFile) {
		this.sessionUnsortedLedFile = sessionUnsortedLedFile;
		destinationSortedLedFile = new File("temp/sorted_leds/", "sorted_" + sessionUnsortedLedFile.getName() + ".led");
		destinationSortedLedFile.getParentFile().mkdirs();
	}

	@Test
	public void test() {
		try {
			System.out.println(">> Testing " + sessionUnsortedLedFile + "(" +
					sessionUnsortedLedFile.length() / 1024.0 / 1024.0 + " MB)...");
			LedHeader sourceHeader = LedHeader.from(sessionUnsortedLedFile);
			LedUtils.sort(sessionUnsortedLedFile, destinationSortedLedFile, NUM_TRANSACTIONS_IN_MEMORY);
			System.out.println("	>> Validating result led...");
			LedHeader destHeader = LedHeader.from(destinationSortedLedFile);
			assertEquals("Sorting caused lost of information: " + sourceHeader.elementCount() + " != " + destHeader.elementCount(),
					sourceHeader.elementCount(), destHeader.elementCount());
			try(LedStream<?> ledStream = new LedReader(destinationSortedLedFile).read(GenericTransaction::new)) {
				long lastId = Long.MIN_VALUE;
				while(ledStream.hasNext()) {
					final long id = Transaction.idOf(ledStream.next());
					assertFalse(sessionUnsortedLedFile + " is not sorted: " + id + " > " + lastId, id > lastId);
					lastId = id;
				}
			}
			System.out.println(">> " + sessionUnsortedLedFile + " sorting was successful.");
		} catch(AssertionError assertionError) {
			return;
		} catch (Throwable e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		System.out.println();
	}
	//C:\Users\naits\Desktop\Monentia\ssh\files\leds\VentaPendienteCobro.S4.DF-202011#36fb2f59-8ebe-4603-b8b3-43fe1ac549b4.led.session


}
