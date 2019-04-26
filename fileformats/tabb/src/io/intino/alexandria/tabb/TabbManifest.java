package io.intino.alexandria.tabb;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

class TabbManifest {
	static final String FileName = ".manifest";
	private final ColumnInfo[] columns;

	static TabbManifest of(File file) throws IOException {
		InputStream inputStream = TabbReader.ZipEntryReader.openEntry(file, FileName);
		TabbManifest tabbManifest = new TabbManifest(readManifest(inputStream).stream().
				map(line -> line.split("\t")).map(l -> new ColumnInfo(l[0], ColumnStream.Type.valueOf(l[1]), Long.parseLong(l[2]), modes(l))).
				toArray(ColumnInfo[]::new));
		inputStream.close();
		return tabbManifest;
	}

	private TabbManifest(ColumnInfo[] columns) {
		this.columns = columns;
	}

	private static String[] modes(String[] fields) {
		return fields.length > 3 ? fields[3].split(":") : new String[0];
	}

	private static List<String> readManifest(InputStream inputStream) throws IOException {
		List<String> collect = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(toList());
		inputStream.close();
		return collect.subList(1, collect.size());
	}

	ColumnInfo[] columns(String... filter) {
		if (filter.length == 0) return this.columns;
		return Arrays.stream(this.columns).filter(c -> Arrays.stream(filter).anyMatch(f -> c.name.equals(f))).toArray(ColumnInfo[]::new);
	}

	long size() {
		return columns[0].size;
	}


	static class ColumnInfo {
		String name;
		ColumnStream.Type type;
		long size;
		String[] modes;

		ColumnInfo(String name, ColumnStream.Type type, long size, String[] modes) {
			this.name = name;
			this.type = type;
			this.size = size;
			this.modes = modes;
		}
	}
}
