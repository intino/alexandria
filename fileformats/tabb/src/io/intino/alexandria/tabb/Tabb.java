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
import static io.intino.alexandria.tabb.ZipHandler.openEntry;
import static io.intino.alexandria.tabb.ZipHandler.writeEntry;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.*;
import static java.util.stream.IntStream.range;
import static java.util.zip.Deflater.BEST_COMPRESSION;

public class Tabb {
	private static final String updateFileSuffix = ".update";
	private static final String removeFile = ".remove";
	private static final String appendFile = ".append";
	private final File file;
	private TabbManifest manifest;

	public Tabb(File file) throws IOException {
		this.file = file;
		this.manifest = TabbManifest.of(file);
	}

	public Iterator<Row> iterator() {
		try {
			return new Iterator<>() {
				TabbReader reader = new TabbReader(file);

				@Override
				public boolean hasNext() {
					boolean hasNext = reader.hasNext();
					if (!hasNext) reader.close();
					return hasNext;
				}

				@Override
				public Row next() {
					Row row = range(0, manifest.columns().length).mapToObj(reader::get).collect(toCollection(Row::new));
					reader.next();
					return row;
				}
			};
		} catch (Exception e) {
			Logger.error(e);
			return Collections.emptyIterator();
		}
	}

	public Tabb update(String id, ColumnInfo column, Object value) {
		try {
			ZipHandler.appendToEntry(file, column.name + updateFileSuffix, id + ";" + value.toString() + "\n");
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
			ZipHandler.appendToEntry(file, removeFile, id + "\n");
			return this;
		} catch (IOException e) {
			Logger.error(e);
		}
		return this;
	}

	public Tabb append(Object[] row) {
		try {
			if (row.length != manifest.columns().length)
				throw new IllegalArgumentException("Row size differ with the columns");
			String id = idOf(stream(row).map(o -> o != null ? o.toString() : "").toArray(String[]::new), indexColumns());
			remove(id);
			removeFromUpdates(id);
			removeFromAppends(id);
			ZipHandler.appendToEntry(file, appendFile, stream(row).map(o -> o != null ? o.toString() : "").collect(joining(";")) + "\n");
			return this;
		} catch (IOException e) {
			Logger.error(e);
		}
		return this;
	}

	public Tabb consolidate() {
		try {
			File temp = Files.createTempDirectory("tabb").toFile();
			List<Tabbc> columns = stream(manifest.columns()).map(c -> new Tabbc(temp, c.name, c.isIndex, c.type, c.features)).collect(Collectors.toList());
			applyUpdatesAndRemoves(columns);
			for (String[] row : loadAppends()) {
				range(0, row.length).forEachOrdered(i -> {
					Tabbc tabbc = columns.get(i);
					Object value = tabbc.type.parse(row[i]);
					if (tabbc.type == Nominal) tabbc.features.add(value.toString());
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
		List<Integer> indexColumns = indexColumns();
		List<Map<String, Object>> updates = loadUpdates(columns);
		List<String> removes = loadRemoves();
		iterator().forEachRemaining(row -> {
			String id = idOf(row, indexColumns, manifest.columns());
			if (removes.contains(id)) return;
			range(0, row.size()).
					forEach(i -> columns.get(i).write(updates.get(i).containsKey(id) ? value(columns.get(i), updates.get(i).get(id)) : row.get(i).bytes()));
		});
	}

	private void updateTabb(File temp, List<Tabbc> columns) throws IOException {
		File tempTabb = new File(temp, this.file.getName());
		createTabbFile(tempTabb, columns);
		Files.move(tempTabb.toPath(), this.file.toPath(), REPLACE_EXISTING);
		this.manifest = TabbManifest.of(file);
	}

	private void removeFromAppends(String id) throws IOException {
		List<Integer> indices = indexColumns();
		if (!ZipHandler.hasEntry(file, appendFile)) return;
		String[] appends = readEntry(appendFile).split("\n");
		writeEntry(file, appendFile, stream(appends).filter(row -> !id.equals(idOf(row.split(";"), indices))).collect(joining("\n")));
	}

	private void removeFromUpdates(String id) throws IOException {
		for (ColumnInfo column : manifest.columns())
			if (ZipHandler.hasEntry(file, column.name + updateFileSuffix)) {
				String updates = readEntry(column.name + updateFileSuffix);
				writeEntry(file, column.name + updateFileSuffix, updates.replaceAll(id + ";.*\n", "\n").trim());
			}
	}

	private List<String> loadRemoves() {
		try {
			if (!ZipHandler.hasEntry(file, removeFile)) return Collections.emptyList();
			return Arrays.asList(readEntry(removeFile).split("\n"));
		} catch (IOException e) {
			Logger.error(e);
			return Collections.emptyList();
		}
	}

	private List<String[]> loadAppends() {
		try {
			if (!ZipHandler.hasEntry(file, appendFile)) return Collections.emptyList();
			return stream(readEntry(appendFile).split("\n")).map(s -> s.split(";")).collect(toList());
		} catch (IOException e) {
			Logger.error(e);
			return Collections.emptyList();
		}
	}

	private List<Map<String, Object>> loadUpdates(List<Tabbc> columns) {
		List<Map<String, Object>> maps = new ArrayList<>();
		for (Tabbc column : columns)
			try {
				if (!ZipHandler.hasEntry(file, column.name + updateFileSuffix)) maps.add(Collections.emptyMap());
				else maps.add(stream(readEntry(column.name + updateFileSuffix).split("\n")).
						map(l -> l.split(";")).
						collect(toMap(keyValue -> keyValue[0], keyValue -> column.type.parse(keyValue[1]), (a, b) -> b)));
			} catch (IOException e) {
				Logger.error(e);
			}
		return maps;
	}

	private String readEntry(String entry) throws IOException {
		return new String(openEntry(file, entry).readAllBytes(), UTF_8);
	}

	private List<Integer> indexColumns() {
		ColumnInfo[] columns = manifest.columns();
		return range(0, columns.length).filter(i -> columns[i].isIndex).boxed().collect(Collectors.toList());
	}

	private String idOf(String[] row, List<Integer> indices) {
		return indices.stream().map(index -> row[index]).collect(joining());
	}

	private String idOf(Row row, List<Integer> indices, ColumnInfo[] columns) {
		return indices.stream().map(i -> columns[i].features[row.get(i).asInteger()]).collect(joining());
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
		return column.type.toByteArray(column.type == Nominal ? column.features.indexOf(object.toString()) : object);
	}

	private InputStream createColumnStream(Tabbc column) throws FileNotFoundException {
		return new FileInputStream(column.file());
	}

	public TabbManifest manifest() {
		return manifest;
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

		private static class SetList extends ArrayList<String> {

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
}
