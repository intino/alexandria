package io.intino.alexandria.epoch;

import io.intino.alexandria.epoch.Timeline.Item;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

import static io.intino.alexandria.epoch.Epoch.chainFileOf;
import static java.util.Arrays.copyOf;
import static java.util.Comparator.comparing;

public class EpochBuilder {
	private final ChainIndex chainIndex;
	private final ChainReader chainReader;
	private final ChainWriter chainWriter;
	private final Map<Long, Stage> stages;

	private EpochBuilder(ChainIndex chainIndex, ChainReader chainReader, ChainWriter chainWriter) {
		this.chainIndex = chainIndex;
		this.chainReader = chainReader;
		this.chainWriter = chainWriter;
		this.stages = new HashMap<>();
	}

	public static EpochBuilder create(File file, int dataSize) throws IOException {
		return new EpochBuilder(
				ChainIndex.create(file, dataSize),
				ChainReader.Null,
				ChainWriter.BulkChainWriter.create(chainFileOf(file), dataSize)
		);
	}

	public static EpochBuilder update(File file) throws IOException {
		return update(ChainIndex.load(file), new RandomAccessFile(chainFileOf(file), "rw"));
	}

	private static EpochBuilder update(ChainIndex index, RandomAccessFile raf) throws IOException {
		return new EpochBuilder(
				index,
				ChainReader.load(raf, index.dataSize()),
				ChainWriter.RandomChainWriter.create(raf, index.dataSize()));
	}

	@SuppressWarnings("WeakerAccess")
	public Timeline timelineOf(long id) {
		return new Timeline(chainIndex, chainReader).of(id);
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

	public EpochBuilder add(long id, Instant instant, byte[] data) {
		if (data == null) return this;
		return add(id, new Item(instant, resize(data)));
	}

	public void close() throws IOException {
		chainIndex.close();
		chainWriter.close();
	}

	private EpochBuilder add(long id, Item item) {
		try {
			Timeline timeline = timelineOf(id);
			if (isUpdatingFile() && timeline.reject(item)) return this;
			append(timeline, id, chainWriter.write(item, true));
		} catch (IOException ignored) {
		}
		return this;
	}

	void append(Timeline timeline, long id, int next) {
		if (timeline.head() < 0) create(timeline, id, next);
		else append(timeline, next);
	}

	private void create(Timeline timeline, long id, int next) {
		this.chainIndex.put(id, next);
		timeline.head(next);
	}

	private void append(Timeline timeline, int next) {
		int cursor = timeline.head();
		while (true) {
			int last = nextOf(cursor);
			if (last == -1) break;
			cursor = last;
		}
		updateNext(cursor, next);
	}


	private int nextOf(int cursor) {
		try {
			return chainReader.recordAt(cursor).next();
		} catch (IOException e) {
			return -1;
		}
	}

	private void updateNext(int cursor, int next) {
		try {
			chainWriter.writeNext(cursor, next);
		} catch (IOException ignored) {
		}
	}

	private byte[] resize(byte[] data) {
		return data.length == chainIndex.dataSize() ? data : copyOf(data, chainIndex.dataSize());
	}

	private boolean isUpdatingFile() {
		return chainIndex instanceof ChainIndex.RandomChainIndex;
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
				checkStageIsCreated();
				items.add(new Item(instant, resize(data)));
				return this;
			}

			private void checkStageIsCreated() {
				if (stages.containsKey(id)) return;
				stages.put(id, this);
			}

			@Override
			public EpochBuilder commit() {
				stages.remove(id);
				items.sort(comparing(o -> o.instant));
				store();
				return EpochBuilder.this;
			}

			@Override
			public int size() {
				return items.size();
			}

			private void store() {
				Timeline timeline = timelineOf(id);
				items = clean(items, isUpdatingFile() ? timeline.last() : Item.Null);
				if (items.size() == 0 || timeline.reject(items.get(0))) return;
				store(timeline);
			}

			private void store(Timeline timeline) {
				try {
					int i = 0;
					for (Item item : items) {
						int cursor = write(item, ++i == items.size());
						if (i == 1) append(timeline, id, cursor);
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

	public interface Stage {
		long id();

		Stage add(Instant instant, byte[] data);

		EpochBuilder commit();

		int size();
	}

}
