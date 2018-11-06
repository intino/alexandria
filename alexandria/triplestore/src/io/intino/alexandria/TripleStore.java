package io.intino.alexandria;

import io.intino.alexandria.logger.Logger;

import java.io.*;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static java.util.stream.Stream.empty;
import static java.util.stream.StreamSupport.stream;

public class TripleStore {
	private final File file;
	private final List<String[]> triples;

	public TripleStore() {
		this.file = null;
		this.triples = emptyList();
	}

	public TripleStore(File file) {
		this.file = file;
		this.triples = contentOf(file).map(this::triple).collect(toList());
	}

	public static String[] valuePatternOf(String[] triple) {
		return new String[]{triple[0], triple[1], null};
	}

	private static String lineOf(String... triple) {
		return triple[0] + ";" + triple[1] + ";" + triple[2] + "\n";
	}

	public File file() {
		return file;
	}

	private Stream<String> contentOf(File file) {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			return reader.lines().collect(toList()).stream();
		} catch (FileNotFoundException e) {
			return empty();
		} catch (IOException e) {
			Logger.error(e.getMessage(), e);
			return empty();
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

	public void save() {
		if (file == null) return;
		file.getParentFile().mkdirs();
		try (BufferedWriter writer = Files.newBufferedWriter(file.toPath())) {
			for (String[] triple : triples) writer.write(lineOf(triple));
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private String[] triple(String line) {
		return line.split(";");
	}

	public static class Builder {
		private OutputStream os;

		public Builder(OutputStream os) {
			this.os = new BufferedOutputStream(os);
		}

		private static byte[] bytesOf(String subject, String predicate, Object value) {
			return lineOf(subject, predicate, value.toString()).getBytes();
		}

		public Builder put(String subject, String predicate, Object value) {
			try {
				os.write(bytesOf(subject, predicate, value));
			} catch (IOException e) {
				Logger.error(e);
			}
			return this;
		}


		public void close() {
			try {
				os.close();
			} catch (IOException e) {
				Logger.error(e);
			}
		}
	}

}
