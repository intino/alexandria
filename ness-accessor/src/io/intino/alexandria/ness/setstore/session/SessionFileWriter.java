package io.intino.alexandria.ness.setstore.session;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class SessionFileWriter {
	private final Map<String, List<Long>> chunks;
	private final DataOutputStream stream;

	public SessionFileWriter(OutputStream outputStream) {
		this.chunks = new LinkedHashMap<>();
		this.stream = new DataOutputStream(new BufferedOutputStream(outputStream));
	}

	public void add(String tank, String timetag, String set, long id) {
		check(tank, timetag, set);
		add(SessionChunkId.chunkId(tank, timetag, set), id);
	}

	private void check(String tank, String timetag, String set) {
		if (tank == null || tank.isEmpty() || timetag == null || timetag.isEmpty() || set == null || set.isEmpty())
			throw new RuntimeException("SetStore: tank, timetag or set is not valid or is empty");
	}

	private void add(String chunkId, long id) {
		if (!chunks.containsKey(chunkId)) chunks.put(chunkId, new ArrayList<>());
		chunks.get(chunkId).add(id);
	}

	public void flush() throws IOException {
		chunks.forEach(this::write);
		chunks.clear();
		stream.flush();
	}

	private void write(String chunkId, List<Long> ids) {
		try {
			stream.writeInt(chunkId.length());
			stream.writeBytes(chunkId);
			stream.writeInt(ids.size());
			Collections.sort(ids);
			for (long id : ids) stream.writeLong(id);
			stream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() throws IOException {
		chunks.forEach(this::write);
		chunks.clear();
		stream.close();
	}
}
