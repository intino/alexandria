package io.intino.alexandria.tabb;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.tabb.TabbManifest.ColumnInfo;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;

import static io.intino.alexandria.tabb.ColumnStream.ColumnExtension;
import static io.intino.alexandria.tabb.ColumnStream.Type.Nominal;
import static io.intino.alexandria.tabb.ZipHandler.readEntry;
import static io.intino.alexandria.tabb.ZipHandler.writeEntry;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;
import static java.util.zip.Deflater.BEST_COMPRESSION;

public class Tabb implements Iterator<Row> {
	private static final String updateFileSuffix = ".update";
	private static final String removeFile = ".remove";
	private static final String appendFile = ".append";
	private final File file;
	private final List<Integer> indexColumns;
	private final List<String> removes;
	private final List<String[]> appends;
	private final List<Map<String, Object>> updatesByColumn;
	private TabbManifest manifest;
	private List<ColumnInfo> columns;
	private VirtualIterator iterator;

	public Tabb(File file) throws IOException {
		this.file = file;
		this.manifest = TabbManifest.of(file);
		this.columns = new ArrayList<>(this.manifest.columns());
		this.indexColumns = indexColumns();
		this.removes = loadRemoves();
		this.appends = loadAppends();
		this.updatesByColumn = loadUpdates();
	}

	public TabbManifest manifest() {
		return manifest;
	}

	@Override
	public boolean hasNext() {
		return iterator().hasNext();
	}

	@Override
	public Row next() {
		return iterator().next();
	}

	public void close() {
		iterator().close();
	}

	public Tabb update(String id, ColumnInfo column, Object value) {
		try {
			ZipHandler.appendToEntry(file, column.name + updateFileSuffix, id + ";" + value.toString() + "\n");
			updatesByColumn.get(indexOf(column)).put(id, value);
			return this;
		} catch (IOException e) {
			Logger.error(e);
		}
		return this;
	}

	public Tabb remove(String id) {
		try {
			removeFromAppends(id);
			removeFromUpdates(id);
			if (!removes.contains(id)) {
				ZipHandler.appendToEntry(file, removeFile, id + "\n");
				removes.add(id);
			}
			return this;
		} catch (IOException e) {
			Logger.error(e);
		}
		return this;
	}

	public Tabb append(Object[] row) {
		try {
			if (row.length != columns.size())
				throw new IllegalArgumentException("Row size differ with the columns");
			String[] stringValues = stream(row).map(o -> o != null ? o.toString() : "null").toArray(String[]::new);
			String id = idOf(stringValues);
			remove(id);
			appends.add(stringValues);
			ZipHandler.appendToEntry(file, appendFile, stream(row).map(o -> o != null ? o.toString() : "null").collect(joining(";")) + "\n");
			return this;
		} catch (IOException e) {
			Logger.error(e);
		}
		return this;
	}

	public Tabb consolidate() {
		try {
			File temp = Files.createTempDirectory("tabb").toFile();
			List<Tabbc> columns = this.columns.stream().map(c -> new Tabbc(temp, c.name, c.isIndex, c.type, c.features.toArray(new String[0]))).collect(Collectors.toList());
			applyUpdatesAndRemoves(columns);
			for (String[] row : appends) {
				range(0, row.length).forEachOrdered(i -> {
					Tabbc tabbc = columns.get(i);
					Object value = row[i].equals("null") ? null : tabbc.type.parse(row[i]);
					if (tabbc.type == Nominal && value != null) tabbc.features.add(value.toString());
					tabbc.write(value(tabbc, value));
				});
			}
			columns.forEach(Tabbc::close);
			updateTabb(temp, columns);
			return this;
		} catch (IOException e) {
			Logger.error(e);
			return this;
		}
	}

	private void applyUpdatesAndRemoves(List<Tabbc> columns) {
		temporalIterator().forEachRemaining(row -> {
			String id = idOf(row);
			if (removes.contains(id)) return;
			range(0, row.size()).
					forEach(i -> columns.get(i).write(this.updatesByColumn.get(i).containsKey(id) ? value(columns.get(i), this.updatesByColumn.get(i).get(id)) : row.get(i).bytes()));
		});
	}

	private void updateTabb(File tempDirectory, List<Tabbc> columns) throws IOException {
		File tempTabb = new File(tempDirectory, this.file.getName());
		createTabbFile(tempTabb, columns);
		Files.move(tempTabb.toPath(), this.file.toPath(), REPLACE_EXISTING);
		deleteDirectory(tempDirectory);
		this.manifest = TabbManifest.of(file);
		this.columns = new ArrayList<>(this.manifest.columns());
	}

	private VirtualIterator iterator() {
		return this.iterator != null ? this.iterator : (this.iterator = new VirtualIterator(file, columns, indexColumns, appends, updatesByColumn, removes));
	}

	private void removeFromAppends(String id) throws IOException {
		if (!ZipHandler.hasEntry(file, appendFile)) return;
		String appendsText = new String(readEntry(file, appendFile), UTF_8);
		if (appendsText.isEmpty()) return;
		String[] appends = appendsText.split("\n");
		writeEntry(file, appendFile, stream(appends).filter(row -> !id.equals(idOf(row.split(";")))).collect(joining("\n")));
		this.appends.removeAll(this.appends.stream().filter(append -> id.equals(idOf(append))).collect(Collectors.toList()));
	}

	private void removeFromUpdates(String id) throws IOException {
		for (ColumnInfo column : columns)
			if (ZipHandler.hasEntry(file, column.name + updateFileSuffix)) {
				String updates = new String(readEntry(file, column.name + updateFileSuffix), UTF_8);
				writeEntry(file, column.name + updateFileSuffix, updates.replaceAll(id + ";.*\n", "\n").trim());
			}
		this.updatesByColumn.forEach(column -> column.remove(id));
	}

	private List<String> loadRemoves() {
		try {
			if (!ZipHandler.hasEntry(file, removeFile)) return new ArrayList<>();
			return new ArrayList<>(Arrays.asList(new String(readEntry(file, removeFile), UTF_8).split("\n")));
		} catch (IOException e) {
			Logger.error(e);
			return new ArrayList<>();
		}
	}

	private List<String[]> loadAppends() {
		try {
			if (!ZipHandler.hasEntry(file, appendFile)) return new ArrayList<>();
			return stream(new String(readEntry(file, appendFile), UTF_8).split("\n")).map(s -> s.split(";")).collect(Collectors.toList());
		} catch (IOException e) {
			Logger.error(e);
			return new ArrayList<>();
		}
	}

	private List<Map<String, Object>> loadUpdates() {
		List<Map<String, Object>> maps = new ArrayList<>();
		for (ColumnInfo column : columns)
			try {
				if (!ZipHandler.hasEntry(file, column.name + updateFileSuffix)) maps.add(new HashMap<>());
				else maps.add(stream(new String(readEntry(file, column.name + updateFileSuffix), UTF_8).split("\n")).
						map(l -> l.split(";")).
						collect(toMap(keyValue -> keyValue[0], keyValue -> column.type.parse(keyValue[1]), (a, b) -> b)));
			} catch (IOException e) {
				Logger.error(e);
			}
		return maps;
	}

	private int indexOf(ColumnInfo column) {
		return range(0, columns.size()).filter(i -> columns.get(i).name.equals(column.name)).findFirst().orElse(-1);
	}

	private List<Integer> indexColumns() {
		return range(0, columns.size()).filter(i -> columns.get(i).isIndex).boxed().collect(Collectors.toList());
	}

	private String idOf(String[] row) {
		return row.length == 0 ? null : indexColumns.stream().map(index -> row[index]).collect(joining());
	}

	private String idOf(Row row) {
		return indexColumns.stream().map(i -> row.get(i).asObject().toString()).collect(joining());
	}

	private void createTabbFile(File file, List<Tabbc> columns) throws IOException {
		ZipOutputStream os = new ZipOutputStream(new FileOutputStream(file));
		os.setLevel(BEST_COMPRESSION);
		for (Tabbc c : columns) writeEntry(os, c.name() + ColumnExtension, createColumnStream(c));
		ColumnInfo[] collect = columns.stream().map(c -> new ColumnInfo(c.name, c.type, c.isIndex, c.size, c.features.toArray(new String[0]))).toArray(ColumnInfo[]::new);
		writeEntry(os, TabbManifest.FileName, TabbManifest.serialize(collect));
		os.close();
	}

	private byte[] value(Tabbc column, Object object) {
		return column.type.toByteArray(column.type == Nominal && object != null ? column.features.indexOf(object.toString()) : object);
	}

	private InputStream createColumnStream(Tabbc column) throws FileNotFoundException {
		return new FileInputStream(column.file());
	}

	private Iterator<Row> temporalIterator() {
		try {
			File temp = Files.createTempDirectory("tabb").toFile();
			File file = new File(temp, this.file.getName());
			Files.copy(this.file.toPath(), file.toPath(), REPLACE_EXISTING);
			return new Iterator<>() {
				TabbReader reader = newReader(file);

				@Override
				public boolean hasNext() {
					boolean hasNext = reader.hasNext();
					if (!hasNext) close();
					return hasNext;
				}

				@Override
				public Row next() {
					return reader.next();
				}

				private void close() {
					reader.close();
					file.delete();
				}
			};
		} catch (IOException e) {
			Logger.error(e);
			return Collections.emptyIterator();
		}
	}

	private TabbReader newReader(File file) {
		try {
			return new TabbReader(file);
		} catch (IOException e) {
			Logger.error(e);
			return null;
		}
	}

	private boolean deleteDirectory(File directoryToBeDeleted) {
		File[] allContents = directoryToBeDeleted.listFiles();
		if (allContents != null) for (File file : allContents) deleteDirectory(file);
		return directoryToBeDeleted.delete();
	}

	private static class Tabbc {
		private final ColumnStream.Type type;
		private final SetList features;
		private final boolean isIndex;
		private String name;
		private File file;
		private long size;
		private BufferedOutputStream os;

		Tabbc(File directory, String name, boolean isIndex, ColumnStream.Type type, String[] features) {
			try {
				this.file = new File(directory, name + ColumnExtension);
				this.os = new BufferedOutputStream(new FileOutputStream(file));
			} catch (IOException e) {
				Logger.error(e);
			}
			this.name = name;
			this.type = type;
			this.features = new SetList(asList(features));
			this.isIndex = isIndex;
		}

		void write(byte[] value) {
			size++;
			try {
				os.write(value);
			} catch (IOException e) {
				Logger.error(e);
			}
		}

		File file() {
			return file;
		}

		public long size() {
			return size;
		}

		public void close() {
			try {
				os.close();
			} catch (IOException e) {
				Logger.error(e);
			}
		}

		public String name() {
			return name;
		}
	}

	static class SetList extends ArrayList<String> {

		public SetList(Collection<? extends String> c) {
			super(c);
		}

		@Override
		public boolean add(String s) {
			if (contains(s)) return false;
			return super.add(s);
		}
	}
}
