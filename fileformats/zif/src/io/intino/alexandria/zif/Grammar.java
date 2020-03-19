package io.intino.alexandria.zif;

import io.intino.alexandria.zif.grammar.Property;
import io.intino.alexandria.zip.Zip;

import java.io.*;
import java.time.Instant;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;


public class Grammar implements Iterable<Property> {
	private List<Property> properties;

	public static final String Filename = ".grammar";

	public Grammar() {
		this.properties = new ArrayList<>();
	}

	public Grammar(File file) throws IOException {
		this();
		load(file);
	}

	public void load(File file) throws IOException {
		Zip zipFile = new Zip(file);
		loadProperties(zipFile.read(Filename));
	}

	public void save(File file) throws IOException {
		Zip zipFile = new Zip(file);
		if (!file.exists()) zipFile.create();
		zipFile.write(Filename, properties.stream().map(p -> p.toString() + "\n").collect(Collectors.joining()));
	}

	public Property get(String name) {
		return properties.stream().filter(p->p.name().equals(name)).findFirst().orElse(null);
	}

	private void loadProperties(String content) throws IOException {
		if (content == null) return;
		String[] lines = content.split("\n");
		Arrays.stream(lines).forEach(line -> {
			if (line.isEmpty()) return;
			append(propertyOf(line));
		});
	}

	private void append(Property property) {
		properties.add(property);
	}

	private Property propertyOf(String line) {
		return new Property(line.split("\t"));
	}

	@Override
	public Iterator<Property> iterator() {
		return properties.iterator();
	}
}
