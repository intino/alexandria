package io.intino.alexandria.sealing;

import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class MessageEventSorterTest {
	@Test
	@Ignore //FIXME
	public void should_sort_blob_of_1000_messages() throws IOException {
		new MessageEventSorter(new File("test-res/eventsort/1000.blob"), new File("temp")).sort(new File("temp/eventsort/result/1000.inl"));
	}

	@Test
	@Ignore //FIXME
	public void should_sort_blob_of_10000_messages() throws IOException {
		new MessageEventSorter(new File("test-res/eventsort/10000.blob"), new File("temp")).sort(new File("temp/eventsort/result/10000.inl"));
	}
}
