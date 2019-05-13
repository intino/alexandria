package io.intino.alexandria.tabb;

import io.intino.alexandria.tabb.generators.ArffFileGenerator;
import io.intino.alexandria.tabb.generators.CsvFileGenerator;
import io.intino.alexandria.tabb.generators.TabbFileGenerator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static io.intino.alexandria.tabb.ColumnStream.ColumnExtension;
import static io.intino.alexandria.tabb.ColumnStream.Type.Nominal;
import static java.lang.String.join;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static java.util.zip.Deflater.BEST_COMPRESSION;


public class TabbBuilder {
	private List<ColumnStream> streams;
	private List<Format> formats;

	public TabbBuilder() {
		streams = new ArrayList<>();
		formats = new ArrayList<>();
	}

	public TabbBuilder add(ColumnStream... streams) {
		this.streams.addAll(asList(streams));
		return this;
	}

	public TabbBuilder add(Format format) {
		formats.add(format);
		return this;
	}

	public void save(File file) throws IOException {
		if (streams.isEmpty()) return;
		List<FileGenerator> generators = generators(file);
		execute(generators);
		generators.forEach(FileGenerator::close);
		createTabbFile(file, buildersIn(generators));
	}

	private void execute(List<FileGenerator> generators) {
		ColumnStream index = streams.get(0);
		while (index.hasNext()) {
			index.next();
			advanceStreamsTo(index.key());
			generators.forEach(b -> b.put(index.key()));
		}
	}

	private void advanceStreamsTo(long key) {
		streams.forEach(stream -> {
			while (stream.hasNext() && valueOf(stream.key()) < key)
				stream.next();
		});
	}

	private long valueOf(Long key) {
		return key != null ? key : Long.MIN_VALUE;
	}

	private List<FileGenerator> generators(File file) {
		List<FileGenerator> generators = new ArrayList<>();
		generators.addAll(builders());
		generators.addAll(exporters(file));
		return generators;
	}

	private List<FileGenerator> exporters(File file) {
		ExporterFactory factory = new ExporterFactory();
		return formats.stream().map((Format f) -> factory.create(f, file)).collect(toList());
	}

	private List<TabbFileGenerator> builders() {
		return streams.stream().map(TabbFileGenerator::new).collect(toList());
	}

	private List<TabbFileGenerator> buildersIn(List<FileGenerator> generators) {
		return generators.stream().filter(g -> g instanceof TabbFileGenerator).map(g -> (TabbFileGenerator) g).collect(toList());
	}

	private void createTabbFile(File file, List<TabbFileGenerator> tabbGenerators) throws IOException {
		ZipOutputStream os = new ZipOutputStream(new FileOutputStream(file));
		os.setLevel(BEST_COMPRESSION);
		for (TabbFileGenerator tabbGenerator : tabbGenerators)
			writeEntry(os, tabbGenerator.name() + ColumnExtension, createColumnStream(tabbGenerator));
		writeEntry(os, TabbManifest.FileName, createManifest(tabbGenerators));
		os.close();
	}

	private void writeEntry(ZipOutputStream zos, String name, InputStream is) throws IOException {
		zos.putNextEntry(new ZipEntry(name));
		byte[] bytes = new byte[1024];
		int length;
		while ((length = is.read(bytes)) >= 0)
			zos.write(bytes, 0, length);
		zos.closeEntry();
	}

	private InputStream createManifest(List<TabbFileGenerator> tabbGenerators) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
		writer.write("name\ttype\tsize\tmode\n");
		for (TabbFileGenerator tabbGenerator : tabbGenerators) {
			writer.write(tabbGenerator.name() + "\t" + tabbGenerator.type().name() + "\t" + tabbGenerator.size());
			addNominalModes(writer, tabbGenerator);
			writer.write("\n");
		}
		writer.close();
		return new ByteArrayInputStream(out.toByteArray());
	}

	private void addNominalModes(BufferedWriter writer, TabbFileGenerator tabbGenerator) throws IOException {
		if (tabbGenerator.type() == Nominal) writer.write("\t" + serialize(tabbGenerator.mode()));
	}

	private String serialize(ColumnStream.Mode mode) {
		return join("|", mode.features);
	}

	private InputStream createColumnStream(TabbFileGenerator column) throws FileNotFoundException {
		return new FileInputStream(column.file());
	}

	private String baseName(String name) {
		return name.endsWith(".tabb") ? name.substring(0, name.lastIndexOf(".")) : name;
	}

	public enum Format {
		csv,
		arff
	}

	private class ExporterFactory {
		private FileGenerator create(TabbBuilder.Format f, File file) {
			if (f.equals(Format.arff)) return new ArffFileGenerator(streams).destination(file.getParentFile(), baseName(file.getName()));
			return new CsvFileGenerator(streams).destination(file.getParentFile(), baseName(file.getName()));
		}
	}

}
