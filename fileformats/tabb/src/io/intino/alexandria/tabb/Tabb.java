package io.intino.alexandria.tabb;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.tabb.TabbManifest.ColumnInfo;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
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
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;
import static java.util.zip.Deflater.BEST_COMPRESSION;

public class Tabb {
	private static final String updateFileSuffix = ".update";
	private static final String removeFileSuffix = ".remove";
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
					return reader.hasNext();
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
			removeUpdates(id);
			ZipHandler.appendToEntry(file, removeFileSuffix, id + "\n");
			return this;
		} catch (IOException e) {
			Logger.error(e);
		}
		return this;
	}

	public Tabb consolidate() {
		try {
			File temp = Files.createTempDirectory("tabb").toFile();
			List<Tabbc> columns = stream(manifest.columns()).map(c -> new Tabbc(temp, c.name, c.type, c.features)).collect(Collectors.toList());
			List<Integer> indexColumns = indexColumns();
			List<Map<String, Object>> updates = loadUpdates(columns);
			List<String> removes = loadRemoves();
			AtomicLong size = new AtomicLong(0);
			iterator().forEachRemaining(row -> {
				String id = idOf(row, indexColumns, manifest.columns());
				if (removes.contains(id)) return;
				range(0, row.size()).
						forEach(i -> columns.get(i).write(updates.get(i).containsKey(id) ? value(columns.get(i), updates.get(i).get(id)) : row.get(i).bytes()));
				size.getAndIncrement();
			});
			columns.forEach(Tabbc::close);
			updateTabb(temp, columns, size);
			return this;
		} catch (IOException e) {
			Logger.error(e);
			return this;
		}
	}

	private void updateTabb(File temp, List<Tabbc> columns, AtomicLong size) throws IOException {
		File tempTabb = new File(temp, this.file.getName());
		createTabbFile(tempTabb, columns, size.get());
		Files.move(tempTabb.toPath(), this.file.toPath(), REPLACE_EXISTING);
		this.manifest = TabbManifest.of(file);
	}

	private void removeUpdates(String id) throws IOException {
		for (ColumnInfo column : manifest.columns())
			if (ZipHandler.hasEntry(file, column.name + updateFileSuffix)) {
				String updates = readEntry(column.name + updateFileSuffix);
				writeEntry(file, column.name + updateFileSuffix, updates.replaceAll(id + ";.*\n", "\n").trim());
			}
	}

	private List<String> loadRemoves() {
		try {
			if (!ZipHandler.hasEntry(file, removeFileSuffix)) return Collections.emptyList();
			return Arrays.asList(readEntry(removeFileSuffix).split("\n"));
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

	private String idOf(Row r, List<Integer> indexColumns, ColumnInfo[] columns) {
		return indexColumns.stream().map(i -> columns[i].features[r.get(i).asInteger()]).collect(Collectors.joining());
	}

	private void createTabbFile(File file, List<Tabbc> columns, Long size) throws IOException {
		ZipOutputStream os = new ZipOutputStream(new FileOutputStream(file));
		os.setLevel(BEST_COMPRESSION);
		for (Tabbc c : columns) writeEntry(os, c.name() + ColumnExtension, createColumnStream(c));
		TabbManifest manifest = TabbManifest.of(this.file);
		stream(manifest.columns()).forEach(column -> column.size = size);
		writeEntry(os, TabbManifest.FileName, TabbManifest.serialize(manifest.columns()));
		os.close();
	}

	private byte[] value(Tabbc column, Object object) {
		return column.type.toByteArray(column.type == Nominal ? asList(column.features).indexOf(object.toString()) : object);
	}


	private InputStream createColumnStream(Tabbc column) throws FileNotFoundException {
		return new FileInputStream(column.file());
	}

	public TabbManifest manifest() {
		return manifest;
	}

	private static class Tabbc {
		private final ColumnStream.Type type;
		private final String[] features;
		private String name;
		private File file;
		private BufferedOutputStream os;

		Tabbc(File directory, String name, ColumnStream.Type type, String[] features) {
			this.type = type;
			this.features = features;
			try {
				this.file = new File(directory, name + ColumnExtension);
				this.os = new BufferedOutputStream(new FileOutputStream(file));
				this.name = name;
			} catch (IOException e) {
				Logger.error(e);
			}
		}

		void write(byte[] value) {
			try {
				os.write(value);
			} catch (IOException e) {
				Logger.error(e);
			}
		}

		File file() {
			return file;
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
}
