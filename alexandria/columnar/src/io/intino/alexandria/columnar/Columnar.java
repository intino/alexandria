package io.intino.alexandria.columnar;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.assa.AssaReader;
import io.intino.alexandria.assa.AssaStream;
import io.intino.alexandria.assa.AssaStream.Merge;
import io.intino.alexandria.columnar.Columnar.Select.FilterOrGet;
import io.intino.alexandria.columnar.exporters.ARFFExporter;
import io.intino.alexandria.columnar.exporters.CSVExporter;
import io.intino.alexandria.triplestore.FileTripleStore;
import io.intino.alexandria.zet.ZetReader;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

public class Columnar {
	private static final String ASSA_FILE = ".assa";
	private File root;

	public Columnar(File root) {
		this.root = root;
	}

	private static File[] zetFilesIn(File directory) {
		return Objects.requireNonNull(directory.listFiles(f -> f.getName().endsWith(".zet")));
	}

	private static File[] directoriesIn(File directory) {
		return Objects.requireNonNull(directory.listFiles(File::isDirectory));
	}

	public String[] columns() {
		return root.list((f, n) -> f.isDirectory());
	}

	public Import load(String column) {
		return directory -> {
			for (File timetag : directoriesIn(directory))
				Merge.of(toAssa(timetag)).save(column, assaFile(timetag, column));
		};
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
						assas.get(timetag).add(new AssaReader<>(assaFile(new File(root, to.value()), column)));
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
		return Arrays.stream(zetFilesIn(directory))
				.map(this::assaStream)
				.collect(toList());
	}

	private AssaStream<String> assaStream(File file) {
		return new AssaStream<String>() {
			ZetReader reader = new ZetReader(file);
			String name = nameOf(file);
			int size = sizeOf(file);

			@Override
			public int size() {
				return size;
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
						return name;
					}
				};
			}

			@Override
			public boolean hasNext() {
				return reader.hasNext();
			}
		};
	}

	private int sizeOf(File file) {
		FileTripleStore tripleStore = new FileTripleStore(new File(file.getParentFile(), ".metadata"));
		String[] triple = tripleStore.matches(nameOf(file), "_size_").findFirst().orElse(null);
		return triple != null ? Integer.parseInt(triple[2]) : 0;
	}

	private String nameOf(File file) {
		return file.getName().substring(0, file.getName().lastIndexOf('.'));
	}

	private File assaFile(File root, String column) {
		return new File(columnDirectory(column), root.getName() + ASSA_FILE);
	}

	private File columnDirectory(String column) {
		File file = new File(root, column);
		file.mkdirs();
		return file;
	}

	public interface Import {
		void from(File directory) throws IOException;
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

}
