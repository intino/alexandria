package io.intino.alexandria.columnar;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.assa.AssaReader;
import io.intino.alexandria.assa.AssaStream;
import io.intino.alexandria.assa.AssaStream.Merge;
import io.intino.alexandria.columnar.Columnar.Select.FilterOrGet;
import io.intino.alexandria.columnar.exporters.ARFFExporter;
import io.intino.alexandria.columnar.exporters.CSVExporter;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.zet.ZFile;
import io.intino.alexandria.zet.ZetReader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Columnar {
	private static final String ASSA_FILE = ".assa";
	private File root;

	public Columnar(File root) {
		this.root = root;
	}

	public String[] columns() {
		return root.list((f, n) -> f.isDirectory());
	}

	public Import load(String column) {
		return f -> {
			if (f.isDirectory()) {
				processDirectory(column, f);
			} else processSet(column, f);
		};
	}

	private void processDirectory(String column, File f) {
		Arrays.stream(directoriesIn(f)).parallel().forEach(timetag -> {
			File assaFile = assaFile(column, timetag.getName());
			if (assaFile.exists()) return;
			toAssa(column, timetag, assaFile);
		});
	}

	private void processSet(String column, File setFile) {
		if (!setFile.getName().endsWith(".zet")) {
			Logger.error("File incompatible");
			return;
		}
		File timetag = setFile.getParentFile();
		File assaFile = assaFile(column, timetag.getName());
		if (assaFile.exists()) return;
		toAssa(column, timetag, assaFile);
	}

	private void toAssa(String column, File source, File destination) {
		try {
			AssaStream of = source.isDirectory() ? Merge.of(toAssa(source)) : assaStream(new ZetInfo(source));
			of.save(column, destination);
			of.close();
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	public Select select(String... columns) {
		return new Select() {
			private Timetag from;

			@Override
			public Select from(Timetag timetag) {
				this.from = timetag;
				return this;
			}

			@Override
			public FilterOrGet to(Timetag to) throws IOException, ClassNotFoundException {
				Map<Timetag, List<AssaReader<String>>> assas = new HashMap<>();
				for (Timetag timetag : from.iterateTo(to)) {
					assas.put(timetag, new ArrayList<>());
					for (String column : columns)
						assas.get(timetag).add(new AssaReader<>(assaFile(column, new File(root, to.value()).getName())));
				}
				return filter(assas);
			}
		};
	}

	private FilterOrGet filter(Map<Timetag, List<AssaReader<String>>> assas) {
		List<Select.ColumnFilter> filters = new ArrayList<>();
		return new FilterOrGet() {
			@Override
			public FilterOrGet filtered(Select.ColumnFilter filter) {
				filters.add(filter);
				return this;
			}

			@Override
			public void intoCSV(File file, ColumnTypes columnTypes) throws IOException {
				new CSVExporter(assas, filters, columnTypes).export(file);
			}

			@Override
			public void intoCSV(File file) throws IOException {
				new CSVExporter(assas, filters, new ColumnTypes()).export(file);
			}

			@Override
			public void intoARFF(File file, ColumnTypes columnTypes) throws IOException {
				new ARFFExporter(assas, filters, columnTypes).export(file);
			}
		};
	}

	private List<AssaStream<String>> toAssa(File directory) {
		return zetInfos(directory).stream()
				.map(this::assaStream)
				.collect(toList());
	}

	private AssaStream<String> assaStream(ZetInfo zet) {
		return new AssaStream<String>() {
			ZetReader reader = new ZetReader(zet.inputStream());

			@Override
			public int size() {
				return zet.size;
			}

			@Override
			public Item<String> next() {
				long key = reader.next();
				return new Item<String>() {
					@Override
					public long key() {
						return key;
					}

					@Override
					public String object() {
						return zet.name;
					}
				};
			}

			@Override
			public boolean hasNext() {
				return reader.hasNext();
			}

			@Override
			public void close() {
				reader.hasNext();
			}
		};
	}


	private List<ZetInfo> zetInfos(File directory) {
		return Arrays.stream(Objects.requireNonNull(directory.listFiles(f -> f.getName().endsWith(".zet")))).map(ZetInfo::new).collect(Collectors.toList());
	}

	private File[] directoriesIn(File directory) {
		return Objects.requireNonNull(directory.listFiles(File::isDirectory));
	}


	private File assaFile(String column, String name) {
		return new File(columnDirectory(column), name + ASSA_FILE);
	}


	private File columnDirectory(String column) {
		File file = new File(root, column);
		file.mkdirs();
		return file;
	}

	public interface Import {

		void from(File directory);
	}

	public interface Select {
		Select from(Timetag timetag) throws IOException, ClassNotFoundException;

		FilterOrGet to(Timetag timetag) throws IOException, ClassNotFoundException;

		interface FilterOrGet extends Into {
			FilterOrGet filtered(ColumnFilter timetag);
		}

		interface ColumnFilter extends Predicate<Long> {
		}

		interface Into {
			void intoCSV(File file, ColumnTypes types) throws IOException;

			void intoCSV(File file) throws IOException;

			void intoARFF(File file, ColumnTypes types) throws IOException;
		}
	}

	private static class ZetInfo {
		final int size;
		final String name;
		InputStream inputStream;

		public ZetInfo(File file) {
			this.name = nameOf(file);
			this.size = sizeOf(file);
			try {
				inputStream = new ByteArrayInputStream(Files.readAllBytes(file.toPath()));
			} catch (IOException e) {
				Logger.error(e);
			}
		}

		InputStream inputStream() {
			return inputStream;
		}

		private int sizeOf(File file) {
			try {
				return (int) new ZFile(file).size();
			} catch (IOException e) {
				Logger.error(e);
				return 0;
			}
		}

		private String nameOf(File file) {
			return file.getName().substring(0, file.getName().lastIndexOf('.'));
		}

	}

}
