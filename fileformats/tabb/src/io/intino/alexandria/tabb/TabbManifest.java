package io.intino.alexandria.tabb;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static io.intino.alexandria.tabb.ColumnStream.Type.Nominal;
import static java.lang.String.join;

public class TabbManifest {
	static final String FileName = ".manifest";
	private final Map<String, ColumnInfo> columns;
	private long size;

	private TabbManifest(ColumnInfo[] columns) {
		this.size = columns[0].size;
		this.columns = Arrays.stream(columns).collect(Collectors.toMap(c -> c.name, c -> c, (e1, e2) -> e1, LinkedHashMap::new));
	}

	public Collection<ColumnInfo> columns() {
		return columns.values();
	}

	public ColumnInfo column(String name) {
		return columns.get(name);
	}

	public long size() {
		return size;
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
		return new TabbManifest(readManifest(file).stream().
				map(line -> line.split("\t")).
				map(l -> new ColumnInfo(l[0], ColumnStream.Type.valueOf(l[1]), Boolean.parseBoolean(l[2]), Long.parseLong(l[3]), modes(l))).
				toArray(ColumnInfo[]::new));
	}


	private static void addNominalModes(BufferedWriter writer, ColumnInfo info) throws IOException {
		if (info.type == Nominal) writer.write("\t" + serialize(info.features));
	}

	private static String serialize(List<String> features) {
		return join("|", features);
	}

	private static String[] modes(String[] fields) {
		return fields.length > 4 ? fields[4].split("\\|") : new String[0];
	}

	private static List<String> readManifest(File tabbFile) throws IOException {
		List<String> collect = Arrays.asList(new String(ZipHandler.readEntry(tabbFile, FileName), StandardCharsets.UTF_8).split("\n"));
		return collect.subList(1, collect.size());
	}

	public static class ColumnInfo {
		String name;
		ColumnStream.Type type;
		boolean isIndex;
		long size;
		List<String> features;

		public ColumnInfo(String name, ColumnStream.Type type, boolean isIndex, long size, String[] features) {
			this.name = name;
			this.type = type;
			this.isIndex = isIndex;
			this.size = size;
			this.features = new Tabb.SetList(Arrays.asList(features));
		}
	}
}
