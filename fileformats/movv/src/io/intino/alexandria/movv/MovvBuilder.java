package io.intino.alexandria.movv;

import io.intino.alexandria.movv.Mov.Item;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

import static io.intino.alexandria.movv.Movv.chainFileOf;
import static java.util.Comparator.comparing;

public class MovvBuilder {
	private final ChainIndex chainIndex;
	private final ChainReader chainReader;
	private final ChainWriter chainWriter;
	private final Map<Long, Stage> stages;

	private MovvBuilder(ChainIndex chainIndex, ChainReader chainReader, ChainWriter chainWriter) {
		this.chainIndex = chainIndex;
		this.chainReader = chainReader;
		this.chainWriter = chainWriter;
		this.stages = new HashMap<>();
	}

	public static MovvBuilder create(File file, int dataSize) throws IOException {
		return new MovvBuilder(
				ChainIndex.create(file, dataSize),
				ChainReader.Null(),
				ChainWriter.BulkChainWriter.create(chainFileOf(file), dataSize)
		);
	}

	public static MovvBuilder update(File file) throws IOException {
		return update(ChainIndex.load(file), new RandomAccessFile(chainFileOf(file), "rw"));
	}

	private static MovvBuilder update(ChainIndex index, RandomAccessFile raf) throws IOException {
		return new MovvBuilder(
				index,
				ChainReader.load(raf, index.dataSize()),
				ChainWriter.RandomChainWriter.create(raf, index.dataSize()));
	}

	public Mov movOf(long id) {
		return new Mov(chainIndex, chainReader).of(id);
	}

	public boolean containsStage(long id){
		return stages.containsKey(id);
	}

	public Stage stageOf(long id) {
		return stages.containsKey(id) ? stages.get(id) : createStage(id);
	}

	public Stream<Stage> stages() {
		return new ArrayList<>(stages.values()).stream();
	}

	public MovvBuilder add(long id, Instant instant, byte[] data) {
		if (data == null) return this;
		try {
			Mov mov = movOf(id);
			if (isUpdatingFile() && mov.reject(new Item(instant, data))) return this;
			mov.append(id, chainWriter.write(new Item(instant, data), true));
		} catch (IOException ignored) {
		}
		return this;
	}

	public void close() throws IOException {
		chainIndex.close();
		chainWriter.close();
	}

	private Stage createStage(long id) {
		return new Stage() {
			List<Item> items = new ArrayList<>();

			@Override
			public long id() {
				return id;
			}

			@Override
			public Stage add(Instant instant, byte[] data) {
				if (data == null) return this;
				items.add(new Item(instant, data));
				if (!stages.containsKey(id)) stages.put(id, this);
				return this;
			}

			@Override
			public MovvBuilder commit() {
				stages.remove(id);
				items.sort(comparing(o -> o.instant));
				store();
				return MovvBuilder.this;
			}

			@Override
			public int size() {
				return items.size();
			}

			private void store() {
				Mov mov = movOf(id);
				items = clean(items, isUpdatingFile() ? mov.last() : Item.Null);
				if (items.size() == 0 || mov.reject(items.get(0))) return;
				store(mov);
			}

			private void store(Mov mov) {
				try {
					int i = 0;
					for (Item item : items) {
						int cursor = write(item, ++i == items.size());
						if (i == 1) mov.append(id, cursor);
					}
				} catch (IOException ignored) {
				}
			}

			private int write(Item item, boolean isTheLast) throws IOException {
				return chainWriter.write(item, isTheLast);
			}

			private List<Item> clean(List<Item> items, Item last) {
				List<Item> result = new ArrayList<>();
				for (Item item : items) {
					if (item.instant.equals(last.instant) || Arrays.equals(item.data, last.data)) continue;
					result.add(item);
					last = item;
				}
				return result;
			}

		};
	}

	private boolean isUpdatingFile() {
		return chainIndex instanceof ChainIndex.RandomChainIndex;
	}

	public interface Stage {
		long id();

		Stage add(Instant instant, byte[] data);

		MovvBuilder commit();

		int size();
	}

}
