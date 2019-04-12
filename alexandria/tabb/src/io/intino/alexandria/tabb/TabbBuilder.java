package io.intino.alexandria.tabb;

import io.intino.alexandria.tabb.exporters.ArffExporter;
import io.intino.alexandria.tabb.exporters.CsvExporter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static io.intino.alexandria.tabb.ColumnStream.ColumnExtension;
import static io.intino.alexandria.tabb.ColumnStream.Type.Nominal;
import static java.util.stream.Collectors.toList;
import static java.util.zip.Deflater.DEFAULT_COMPRESSION;


public class TabbBuilder {
	private List<ColumnStream> streams;
	private List<Format> formats;

	public TabbBuilder() {
		streams = new ArrayList<>();
		formats = new ArrayList<>();
	}

	public TabbBuilder add(ColumnStream stream) {
		streams.add(stream);
		return this;
	}

	public TabbBuilder add(Format format) {
		formats.add(format);
		return this;
	}

	public void save(File directory, String name) throws IOException {
		if (streams.isEmpty()) return;
		List<Generator> generators = generators();
		setDestinationIn(generators, directory, name);
		execute(generators);
		generators.forEach(Generator::close);
		createTabbFile(new File(directory, name + ".tabb"), buildersIn(generators));
	}

	private void setDestinationIn(List<Generator> generators, File directory, String name) {
		generators.stream().filter(g -> g instanceof Exporter).forEach(g -> ((Exporter) g).destination(directory, name));
	}

	private void execute(List<Generator> generators) {
		ColumnStream index = streams.get(0);
		while (index.hasNext()) {
			index.next();
			advanceStreamsTo(index.key());
			generators.forEach(b -> b.put(index.key()));
		}
	}

	private List<Generator> generators() {
		List<Generator> generators = new ArrayList<>();
		generators.addAll(builders());
		generators.addAll(exporters());
		return generators;
	}

	private List<Exporter> exporters() {
		ExporterFactory factory = new ExporterFactory();
		return formats.stream().map(factory::create).collect(toList());
	}

	private List<Builder> builders() {
		return streams.stream().map(Builder::new).collect(toList());
	}

	private List<Builder> buildersIn(List<Generator> generators) {
		return generators.stream().filter(g -> g instanceof Builder).map(g -> (Builder) g).collect(toList());
	}

	private void advanceStreamsTo(long key) {
		streams.forEach(stream -> {
			while (stream.key() != null && stream.key() < key)
				stream.next();
		});
	}

	private void createTabbFile(File file, List<Builder> builders) throws IOException {
		ZipOutputStream os = new ZipOutputStream(new FileOutputStream(file));
		os.setLevel(DEFAULT_COMPRESSION);
		for (Builder builder : builders)
			writeEntry(os, builder.name() + ColumnExtension, createColumnStream(builder));
		writeEntry(os, TabbInfo.FileName, createInfo(builders));
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

	private InputStream createInfo(List<Builder> builders) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
		writer.write("name,type,size,mode\n");
		for (Builder builder : builders) {
			writer.write(builder.name() + "," + builder.type().name() + "," + builder.size());
			addNominalModes(writer, builder);
			writer.write("\n");
		}
		writer.close();
		return new ByteArrayInputStream(out.toByteArray());
	}

	private void addNominalModes(BufferedWriter writer, Builder builder) throws IOException {
		if (builder.type() == Nominal) writer.write("," + serialize(builder.mode()));
	}

	private String serialize(ColumnStream.Mode mode) {
		return String.join(":", mode.features);
	}

	private InputStream createColumnStream(Builder column) throws FileNotFoundException {
		return new FileInputStream(column.file());
	}

	private enum Format {
		csv,
		arff
	}

	private class ExporterFactory {
		private Exporter create(TabbBuilder.Format f) {
			if (f.equals(Format.arff)) return new ArffExporter(streams);
			return new CsvExporter(streams);
		}
	}

}
