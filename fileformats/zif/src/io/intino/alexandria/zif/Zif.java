package io.intino.alexandria.zif;

import io.intino.alexandria.zip.Zip;

import java.io.*;
import java.time.Instant;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.GZIPOutputStream;

import static java.util.stream.Collectors.toList;


public class Zif implements Iterable<Zif.Assertion> {
	private Grammar grammar;
	private List<Assertion> assertions;

	public Zif() {
		this.assertions = new ArrayList<>();
		this.grammar = new Grammar();
	}

	public Zif(File file) throws IOException {
		this();
		load(file);
	}

	public Grammar grammar() {
		return grammar;
	}

	public void load(File file) throws IOException {
		Zip zipFile = new Zip(file);
		loadAssertions(zipFile.read(file.getName()));
		loadGrammar(file);
	}

	public List<Assertion> get(String id) {
		return assertions.stream().filter(a->a.id.equals(id)).collect(toList());
	}

	public Search search(String property, String text) {
		return search(a->a.property.contains(property) && a.value.contains(text));
	}

	public Search search(Predicate<Assertion> predicate) {
		return new Search(predicate);
	}

	public void append(Assertion assertion) {
		assertions.add(assertion);
	}

	public void append(String id, String property, String value) {
		append(new Assertion(Instant.now(), id, property, value));
	}

	public void save(File file) throws IOException {
		Zip zip = new Zip(file);
		if (!file.exists()) zip.create();
		zip.write(file.getName(), assertions.stream().map(a -> a.toString() + "\n").collect(Collectors.joining()));
		grammar.save(file);
	}

	private void loadAssertions(String content) throws IOException {
		if (content == null) return;
		String[] lines = content.split("\n");
		Arrays.stream(lines).forEach(line -> {
			if (line.isEmpty()) return;
			append(assertionOf(line));
		});
	}

	private void loadGrammar(File file) throws IOException {
		grammar = new Grammar(file);
	}

	private Assertion assertionOf(String line) {
		return new Assertion(line.split("\t"));
	}

	@Override
	public Iterator<Assertion> iterator() {
		return assertions.iterator();
	}

	public class Search {
		private final Set<String> ids = new HashSet<>();

		Search(Predicate<Assertion> predicate) {
			this.apply(predicate);
		}


		private void apply(Predicate<? super Assertion> predicate) {
			assertions.stream()
				.filter(predicate)
				.forEach(a -> ids.add(a.id()));
		}

		public boolean contains(String id) {
			return ids.contains(id);
		}

		public int size() {
			return ids.size();
		}

		public List<String> asList() {
			return new ArrayList<>(ids);
		}

		public Iterator<String> iterator() {
			return ids.iterator();
		}

		public String first() {
			return ids.isEmpty() ? "" : ids.iterator().next();
		}

		public Stream<String> stream() {
			return ids.stream();
		}
	}

	public static class Assertion {
		private final Instant ts;
		private final String id;
		private final String property;
		private final String value;

		public Assertion(Instant ts, String id, String property, String value) {
			this.ts = ts;
			this.id = id;
			this.property = property;
			this.value = value;
		}

		public Assertion(String[] data) {
			this(Instant.parse(data[0]), data[1], data[2], data[3]);
		}

		public Instant ts() {
			return ts;
		}

		public String id() {
			return id;
		}

		public String property() {
			return property;
		}

		public String value() {
			return value;
		}

		@Override
		public String toString() {
			return ts.toString() + "\t" + id + "\t" + property + "\t" + value;
		}

	}
}
