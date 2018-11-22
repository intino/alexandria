package io.intino.alexandria.columnar;

import com.opencsv.CSVWriter;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.assa.AssaReader;
import io.intino.alexandria.assa.AssaStream;
import io.intino.alexandria.assa.AssaStream.Merge;
import io.intino.alexandria.columnar.Columnar.Select.FilterOrGet;
import io.intino.alexandria.triplestore.FileTripleStore;
import io.intino.alexandria.zet.ZetReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.lang.String.valueOf;

public class Columnar {

	private static final String ASSA_FILE = ".assa";
	private static final String CSV_NULL_VALUE = "NULL";
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

	public Import load(String column) {
		return directory -> {
			for (File timetag : directoriesIn(directory))
				Merge.of(toAssa(timetag)).save(column, assaFile(timetag, column));
		};
	}

	public Select select(String... columns) {
		return timetag -> {
			List<AssaReader<String>> assas = new ArrayList<>();
			for (String column : columns)
				assas.add(new AssaReader<>(assaFile(new File(root, timetag.value()), column)));
			return filter(assas);
		};
	}

	private FilterOrGet filter(List<AssaReader<String>> assas) {
		List<Select.ColumnFilter> filters = new ArrayList<>();
		return new FilterOrGet() {
			@Override
			public FilterOrGet filtered(Select.ColumnFilter filter) {
				filters.add(filter);
				return this;
			}

			@SuppressWarnings("deprecation")
			@Override
			public void intoCSV(File file) throws IOException {
				CSVWriter csvWriter = new CSVWriter(new FileWriter(file), ';');
				csvWriter.writeNext(headers(assas));
				AssaMerger merger = new AssaMerger(assas, filters);
				while (merger.hasNext()) {
					String[] next = merger.next();
					if (next == null) break;
					csvWriter.writeNext(next);
				}
				csvWriter.close();
			}

			private String[] headers(List<AssaReader<String>> assas) {
				List<String> header = new ArrayList<>();
				header.add("id");
				header.addAll(assas.stream().map(AssaReader::name).collect(Collectors.toList()));
				return header.toArray(new String[0]);
			}
		};
	}

	private List<AssaStream<String>> toAssa(File directory) {
		return Arrays.stream(zetFilesIn(directory))
				.map(this::assaStream)
				.collect(Collectors.toList());
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
		FilterOrGet from(Timetag timetag) throws IOException, ClassNotFoundException;

		interface FilterOrGet extends IntoCSV {
			FilterOrGet filtered(ColumnFilter timetag);
		}

		interface ColumnFilter extends Predicate<Long> {

		}

		interface IntoCSV {
			void intoCSV(File file) throws IOException;
		}
	}

	private static class AssaMerger {
		private final List<TemporalReader> readers;
		private final List<Select.ColumnFilter> filters;

		AssaMerger(List<AssaReader<String>> readers, List<Select.ColumnFilter> filters) {
			this.readers = readers.stream().map(TemporalReader::new).collect(Collectors.toList());
			this.filters = filters;
		}

		String[] next() {
			TemporalReader lowest = getFilteredActiveCandidate();
			if (lowest == null) return null;
			String[] fields = new String[readers.size() + 1];
			long lowestKey = lowest.current.key();
			fields[0] = valueOf(lowestKey);
			for (int i = 0; i < readers.size(); i++) {
				TemporalReader reader = readers.get(i);
				if (reader.current != null && reader.current.key() == lowestKey) {
					fields[i + 1] = reader.current.object().toString();
					reader.next();
				} else fields[i + 1] = CSV_NULL_VALUE;
			}
			return fields;
		}

		boolean hasNext() {
			return readers.stream().anyMatch(TemporalReader::hasNext);
		}

		private TemporalReader getFilteredActiveCandidate() {
			TemporalReader candidate;
			while (!satisfies(candidate = getCandidate())) {
				if (candidate == null) break;
				candidate.next();
			}
			return candidate;
		}

		private boolean satisfies(TemporalReader candidate) {
			return candidate != null && filters.stream().allMatch(filter -> filter.test(candidate.current.key()));
		}

		private TemporalReader getCandidate() {
			List<TemporalReader> readers = getActiveCandidates();
			if (readers.isEmpty()) return null;
			long reference = readers.get(0).current.key();
			TemporalReader candidate = readers.get(0);
			for (int i = 1; i < readers.size(); i++) {
				if (readers.get(i).current == null) continue;
				long newId = readers.get(i).current.key();
				if (newId < reference) {
					reference = newId;
					candidate = readers.get(i);
				}
			}
			return candidate;
		}

		private List<TemporalReader> getActiveCandidates() {
			return readers.stream().filter(TemporalReader::hasNext).collect(Collectors.toList());
		}

		private class TemporalReader {

			private final AssaReader reader;
			private AssaStream.Item current;

			TemporalReader(AssaReader reader) {
				this.reader = reader;
				current = reader.next();
			}

			void next() {
				this.current = reader.next();
			}

			public boolean hasNext() {
				return reader.hasNext();
			}
		}
	}

}
