package io.intino.alexandria.tabb;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static io.intino.alexandria.tabb.ColumnStream.Type.Nominal;
import static java.lang.String.join;
import static java.util.stream.Collectors.toList;

public class TabbManifest {
	static final String FileName = ".manifest";
	private final ColumnInfo[] columns;

	private TabbManifest(ColumnInfo[] columns) {
		this.columns = columns;
	}

	public ColumnInfo[] columns(String... filter) {
		if (filter.length == 0) return this.columns;
		return Arrays.stream(this.columns).filter(c -> Arrays.stream(filter).anyMatch(f -> c.name.equals(f))).toArray(ColumnInfo[]::new);
	}

	public long size() {
		return columns[0].size;
	}

	static InputStream serialize(ColumnInfo[] columns) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
		writer.write("name\ttype\tindex\tsize\tmode\n");
		for (ColumnInfo column : columns) {
			writer.write(column.name + "\t" + column.type.name() + "\t" + column.isIndex + "\t" + column.size);
			addNominalModes(writer, column);
			writer.write("\n");
		}
		writer.close();
		return new ByteArrayInputStream(out.toByteArray());
	}

	static TabbManifest of(File file) throws IOException {
		InputStream inputStream = inputStream(file);
		TabbManifest tabbManifest = new TabbManifest(readManifest(inputStream).stream().
				map(line -> line.split("\t")).
				map(l -> new ColumnInfo(l[0], ColumnStream.Type.valueOf(l[1]), Boolean.parseBoolean(l[2]), Long.parseLong(l[3]), modes(l))).
				toArray(ColumnInfo[]::new));
		inputStream.close();
		return tabbManifest;
	}

	static InputStream inputStream(File file) throws IOException {
		return ZipHandler.openEntry(file, FileName);
	}

	private static void addNominalModes(BufferedWriter writer, ColumnInfo info) throws IOException {
		if (info.type == Nominal) writer.write("\t" + serialize(info.features));
	}

	private static String serialize(String[] features) {
		return join("|", features);
	}

	private static String[] modes(String[] fields) {
		return fields.length > 4 ? fields[4].split("\\|") : new String[0];
	}

	private static List<String> readManifest(InputStream inputStream) throws IOException {
		List<String> collect = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(toList());
		inputStream.close();
		return collect.subList(1, collect.size());
	}

	public static class ColumnInfo {
		String name;
		ColumnStream.Type type;
		boolean isIndex;
		long size;
		String[] features;

		public ColumnInfo(String name, ColumnStream.Type type, boolean isIndex, long size, String[] features) {
			this.name = name;
			this.type = type;
			this.isIndex = isIndex;
			this.size = size;
			this.features = features;
		}
	}
}
