package io.intino.alexandria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static java.util.stream.StreamSupport.stream;

public class TripleStore {
	private final File file;
	private final List<String[]> triples;
	private final BufferedReader reader;

	public TripleStore(File file) {
		this.file = file;
		reader = readerOf(file);
		this.triples = reader.lines().map(this::triple).collect(toList());
		try {
			reader.close();
		} catch (IOException e) {
			LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME).error(e.getMessage(), e);
		}
	}

	public void put(String subject, String predicate, Object value) {
		put(new String[]{subject, predicate, value.toString()});
	}

	private void put(String... triple) {
		find(valuePatternOf(triple)).forEach(this::remove);
		triples.add(triple);
	}

	private void remove(int index) {
		triples.remove(index);
	}

	public static String[] valuePatternOf(String[] triple) {
		return new String[]{triple[0], triple[1], null};
	}

	public Stream<String[]> all() {
		return triples.stream();
	}

	public Stream<String[]> matches(String... pattern) {
		return stream(find(normalize(pattern)).spliterator(), false).map(triples::get);
	}

	private String[] normalize(String[] pattern) {
		String[] result = new String[]{null, null, null};
		System.arraycopy(pattern, 0, result, 0, pattern.length);
		return result;
	}

	private Iterable<Integer> find(String[] pattern) {
		return () -> new Iterator<Integer>() {
			private int index = 0;

			@Override
			public boolean hasNext() {
				while (index < triples.size()) {
					if (match(triples.get(index))) break;
					index++;
				}
				return index < triples.size();
			}

			private boolean match(String[] triple) {
				return range(0, 3).allMatch(with(triple));
			}

			private IntPredicate with(String[] triple) {
				return i -> pattern[i] == null || pattern[i].equals(triple[i]);
			}

			@Override
			public Integer next() {
				return index++;
			}
		};

	}

	public void save() throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(file.toPath())) {
			for (String[] triple : triples) writer.write(lineOf(triple));
		}
	}

	private String lineOf(String[] triple) {
		return triple[0] + ";" + triple[1] + ";" + triple[2] + "\n";
	}

	private String[] triple(String line) {
		return line.split(";");
	}

	private BufferedReader readerOf(File file) {
		return new BufferedReader(file.exists() ? fileReaderOf(file) : emptyReader());
	}

	private Reader fileReaderOf(File file) {
		try {
			return new FileReader(file);
		} catch (FileNotFoundException e) {
			return emptyReader();
		}
	}

	private Reader emptyReader() {
		return new InputStreamReader(new ByteArrayInputStream(new byte[0]));
	}
}
