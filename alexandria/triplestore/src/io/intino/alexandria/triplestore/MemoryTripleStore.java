package io.intino.alexandria.triplestore;

import io.intino.alexandria.logger.Logger;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static java.util.stream.Stream.empty;
import static java.util.stream.StreamSupport.stream;

public class MemoryTripleStore implements TripleStore {
	private final List<String[]> triples;

	public MemoryTripleStore() {
		this.triples = emptyList();
	}

	public MemoryTripleStore(InputStream is) {
		this.triples = contentOf(is).map(this::triple).collect(toList());
	}

	private static String[] valuePatternOf(String[] triple) {
		return new String[]{triple[0], triple[1], null};
	}

	private Stream<String> contentOf(InputStream is) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
			return reader.lines().collect(toList()).stream();
		} catch (IOException e) {
			Logger.error(e.getMessage(), e);
			return empty();
		}
	}

	@Override
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

	@Override
	public Stream<String[]> all() {
		return triples.stream();
	}

	@Override
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

	private String[] triple(String line) {
		return line.split(";");
	}

	public void save(OutputStream outputStream) {
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
			for (String[] triple : triples) writer.write(TripleStore.lineOf(triple));
		} catch (IOException e) {
			Logger.error(e);
		}
	}

}
