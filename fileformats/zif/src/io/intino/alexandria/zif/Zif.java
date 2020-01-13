package io.intino.alexandria.zif;

import java.io.*;
import java.time.Instant;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static java.util.stream.Collectors.toList;


public class Zif {
	private List<Assertion> assertions;

	public Zif() {
		this.assertions = new ArrayList<>();
	}

	public Zif(InputStream is) throws IOException {
		this();
		this.load(is);
	}

	public Zif(File file) throws IOException {
		this(new FileInputStream(file));
	}

	public void load(File file) throws IOException {
		load(new FileInputStream(file));
	}

	public void load(InputStream is) throws IOException {
		try (BufferedReader reader = readerOf(is)) {
			while (true) {
				String line = reader.readLine();
				if (line == null) break;
				if (line.isEmpty()) continue;
 				append(assertionOf(line));
			}
		}
	}

	public void append(Assertion assertion) {
		assertions.add(assertion);
	}

	public void append(String id, String property, String value) {
		append(new Assertion(Instant.now(), id, property, value));
	}

	public void save(OutputStream os) throws IOException {
		try (BufferedWriter writer = writerOf(os)) {
			assertions.forEach(a-> save(writer, a));
		}
	}

	public void save(File file) throws IOException {
		save(new FileOutputStream(file));
	}

	public List<Assertion> get(String id) {
		return assertions.stream().filter(a->a.id.equals(id)).collect(toList());
	}

	private Assertion assertionOf(String line) {
		return new Assertion(line.split("\t"));
	}

	private BufferedReader readerOf(InputStream is) throws IOException {
		return new BufferedReader(new InputStreamReader(new GZIPInputStream((is))));
	}

	private BufferedWriter writerOf(OutputStream os) throws IOException {
		return new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream((os))));
	}

	private void save(BufferedWriter writer, Assertion assertion) {
		try {
			writer.write(assertion.toString() + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Search search(String property, String text) {
		return search(a->a.property.contains(property) && a.value.contains(text));
	}

	public Search search(Predicate<Assertion> predicate) {
		return new Search(predicate);
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
