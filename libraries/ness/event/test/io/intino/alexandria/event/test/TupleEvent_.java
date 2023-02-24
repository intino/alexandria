package io.intino.alexandria.event.test;

import io.intino.alexandria.event.EventStream;
import io.intino.alexandria.event.EventWriter;
import io.intino.alexandria.event.tuple.TupleEvent;
import io.intino.alexandria.ztp.Tuple;
import io.intino.alexandria.ztp.TupleReader;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class TupleEvent_ {

	private static final List<Tuple> Ref = loadRefFile();

	@Test
	public void write_events_into_ztp_from_scratch() throws IOException {
		File file = new File("../temp/ztp_from_scratch.ztp");
		file.delete();
		file.getParentFile().mkdirs();
		EventWriter.write(file, Ref.stream().map(TupleEvent::new));
		compareWithRef(file);
	}

	@Test
	public void append_events_into_an_existing_ztp() throws IOException {
		List<Tuple> tuplesAlreadyWritten = Ref.subList(0, Ref.size() / 2);
		File file = new File("../temp/append_ztp.ztp");
		file.delete();
		file.getParentFile().mkdirs();
		setupZtpWithSomeTuplesInIt(file, tuplesAlreadyWritten);
		EventWriter.append(file, Ref.stream().skip(tuplesAlreadyWritten.size()).map(TupleEvent::new));
		compareWithRef(file);
	}

	private void setupZtpWithSomeTuplesInIt(File file, List<Tuple> tuples) throws IOException {
		EventWriter.write(file, tuples.stream().map(TupleEvent::new));
	}

	private void compareWithRef(File file) throws IOException {
		List<TupleEvent> events = EventStream.<TupleEvent>of(file).collect(Collectors.toList());
		assertEquals(Ref.size(), events.size());

		for(int i = 0;i < events.size();i++) {
			assertEquals(Ref.get(i), events.get(i).toTuple());
		}
	}

	private static List<Tuple> loadRefFile() {
		List<Tuple> tuples = new ArrayList<>();
		try(TupleReader reader = new TupleReader(TupleEvent_.class.getResourceAsStream("/tuples.tsv"))) {
			while(reader.hasNext()) tuples.add(reader.next());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return tuples;
	}
}
