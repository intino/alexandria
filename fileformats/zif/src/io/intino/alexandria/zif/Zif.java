package io.intino.alexandria.zif;

import java.io.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class Zif {
	public final static String Identifier = "id/";
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

	public List<Assertion> get(String id) {
		return assertions.stream()
				.filter(a-> a.id().equals(id))
				.collect(toList());
	}

	public boolean exists(String id) {
		return new Search(a -> a.equalTo(id)).size() == 1;
	}

	public Search search(String text) {
		return new Search(a -> a.contains(text));
	}

	public Search search(String property, Condition condition) {
		return new Search(a->a.property().equals(property), condition);
	}

	public class Search {
		private final Set<String> ids = new HashSet<>();
		private final Condition condition;

		Search(Predicate<? super Assertion> predicate) {
			this.condition = assertion -> true;
			this.apply(predicate);
		}

		Search(Predicate<? super Assertion> predicate, Condition condition) {
			this.condition = condition;
			this.apply(predicate);
		}

		private void apply(Predicate<? super Assertion> predicate) {
			assertions.stream()
				.filter(predicate)
				.forEach(this::evaluate);
		}

		private boolean evaluate(Assertion assertion) {
			return condition.evaluate(assertion) ?
					ids.add(assertion.id()) :
					ids.remove(assertion.id());
		}

		public boolean contains(String id) {
			return ids.contains(id);
		}

		public int size() {
			return ids.size();
		}

		public String describe() {
			return describe("");
		}

		public String describe(String property) {
			StringBuilder result = new StringBuilder();
			for (String id : ids) result.append(describe(property, id));
			return result.length() > 0 ? result.toString().substring(1) : "";
		}

		private String describe(String property, String id) {
			return "\n[" + id  + "]\n" +
					describe(property, assertions.stream()
							.filter(a->a.id().equals(id))
							.filter(a->a.property().startsWith(property))
			);
		}
		private String describe(String property, Stream<Assertion> stream) {
			return stream
					.map(a->a.property().substring(property.length()) + ": " + a.value())
					.collect(joining("\n")) + "\n";
		}

		public List<String> asList() {
			return new ArrayList<>(ids);
		}
	}

	public interface Condition {
		boolean evaluate(Assertion assertion);
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

		public boolean equalTo(String value) {
			return id.equals(value) || this.property.startsWith(Identifier) && this.value.equalsIgnoreCase(value);
		}

		public boolean contains(String value) {
			return id.equals(value) || this.property.startsWith(Identifier) && this.value.contains(value);
		}

	}
}
