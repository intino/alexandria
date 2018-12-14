package io.intino.alexandria.columnar;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.assa.AssaBuilder;
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

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class Columnar {
	private static final String ASSA_FILE = ".assa";
	private final File root;

	public Columnar(File root) {
		this.root = root;
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
			public FilterOrGet to(Timetag to) throws IOException {
				Map<Timetag, List<AssaReader>> assas = new HashMap<>();
				for (Timetag timetag : from.iterateTo(to)) {
					assas.put(timetag, new ArrayList<>());
					for (String column : columns)
						assas.get(timetag).add(new AssaReader(assaFile(column, new File(root, to.value()).getName())));
				}
				return filter(assas);
			}
		};
	}

	public void importColumn(File file) {
		stream(directoriesIn(file)).parallel().forEach(this::build);
	}

	private void build(File source) {
		try {
			List<ZetInfo> zets = streamOf(source);
			AssaBuilder builder = new AssaBuilder(zets.stream().map(z -> z.name).collect(toList()));
			builder.put(Merge.of(zets.stream().map(this::assaStream).collect(toList())));
			builder.save(destinationOf(source));
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private File destinationOf(File source) {
		String timetag = source.getName();
		String tank = source.getParentFile().getName();
		File file = new File(root, tank + "/" + timetag + ".assa");
		file.getParentFile().mkdirs();
		return file;
	}

	private FilterOrGet filter(Map<Timetag, List<AssaReader>> assas) {
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

	private List<ZetInfo> streamOf(File directory) {
		return zetInfos(directory).stream()
				.sorted(Comparator.comparing(s -> s.size))
				.collect(toList());
	}

	private AssaStream assaStream(ZetInfo zet) {
		return new AssaStream() {
			ZetReader reader = new ZetReader(zet.inputStream());

			@Override
			public int size() {
				return zet.size;
			}

			@Override
			public Item next() {
				long key = reader.next();
				return new Item() {
					@Override
					public long key() {
						return key;
					}

					@Override
					public String value() {
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
			}
		};
	}


	private List<ZetInfo> zetInfos(File directory) {
		return stream(Objects.requireNonNull(directory.listFiles(f -> f.getName().endsWith(".zet")))).map(ZetInfo::new).collect(Collectors.toList());
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
