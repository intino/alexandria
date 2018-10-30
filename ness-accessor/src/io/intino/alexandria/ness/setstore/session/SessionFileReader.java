package io.intino.alexandria.ness.setstore.session;


import io.intino.sezzet.operators.SetStream;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class SessionFileReader {

	private static final int LONG_SIZE = 8;
	private static final int INT_SIZE = 4;
	private final File file;
	private final List<Chunk> chunks = new ArrayList<>();

	public SessionFileReader(File file) throws IOException {
		this.file = file;
		readStructure(file);
	}

	@SuppressWarnings("InfiniteLoopStatement")
	private void readStructure(File file) throws IOException {
		try (DataInputStream stream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
			long pos = 0;
			while (true) {
				String chunkId = readString(stream);
				int idSize = stream.readInt();
				stream.skipBytes(idSize * LONG_SIZE);
				pos += INT_SIZE + chunkId.length() + INT_SIZE;
				chunks.add(new Chunk(chunkId, idSize, pos));
				pos += idSize * LONG_SIZE;
			}
		} catch (EOFException ignored) {
		}
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	private String readString(DataInputStream stream) throws IOException {
		int size = stream.readInt();
		byte[] bytes = new byte[size];
		stream.read(bytes);
		return new String(bytes, StandardCharsets.UTF_8);
	}

	public List<Chunk> chunks() {
		return chunks;
	}

	public List<Chunk> chunks(String chunkId) {
		return chunks.stream()
				.filter(c -> c.id.equals(chunkId))
				.collect(toList());
	}

	public class Chunk {
		private final String id;
		long position;
		private int idSize;

		Chunk(String chunkId, int idSize, long position) {
			this.id = chunkId;
			this.idSize = idSize;
			this.position = position;
		}

		public String id() {
			return id;
		}

		public String tank() {
			return SessionChunkId.tankOf(id);
		}

		public String timetag() {
			return SessionChunkId.timetagOf(id);
		}

		public String set() {
			return SessionChunkId.setOf(id);
		}

		public SetStream stream() {
			try {
				RandomAccessFile access = new RandomAccessFile(file, "r");
				access.seek(position);
				DataInputStream stream = new DataInputStream(new BufferedInputStream(new FileInputStream(access.getFD())));
				return new SetStream() {
					int count = 0;
					long current = -1;

					@Override
					public long current() {
						return current;
					}

					@Override
					public long next() {
						try {
							if (!hasNext()) return current = -1;
							count++;
							return current = stream.readLong();
						} catch (EOFException e) {
							return -1;
						} catch (IOException e) {
							e.printStackTrace();
							return -1;
						}
					}

					@Override
					public boolean hasNext() {
						boolean hasNext = count < idSize;
						if (!hasNext) {
							try {
								access.close();
								stream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						return hasNext;
					}
				};
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
}
