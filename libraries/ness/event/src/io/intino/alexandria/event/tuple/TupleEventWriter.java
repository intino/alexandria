package io.intino.alexandria.event.tuple;

import io.intino.alexandria.event.AbstractEventWriter;
import io.intino.alexandria.ztp.ZtpStream;
import io.intino.alexandria.ztp.ZtpWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

public class TupleEventWriter extends AbstractEventWriter<TupleEvent> {

	public TupleEventWriter(File file) {
		super(file);
	}

	@Override
	protected File merge(Stream<TupleEvent> data) throws IOException {
		File temp = tempFile();
		try(ZtpWriter writer = new ZtpWriter(temp)) {
			try(Stream<TupleEvent> merged = mergeFileWith(data)) {
				Iterator<TupleEvent> events = merged.iterator();
				while (events.hasNext()) {
					writer.write(events.next().toString());
				}
			}
		}
		return temp;
	}
}
