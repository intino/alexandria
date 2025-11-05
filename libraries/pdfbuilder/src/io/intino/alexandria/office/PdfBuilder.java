package io.intino.alexandria.office;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;

import static java.util.Objects.requireNonNull;

public class PdfBuilder {

	public static PdfBuilder create(File file) {
		return new PdfBuilder(file, new ApachePOIBackend());
	}

	public static PdfBuilder create(File file, Backend backend) {
		return new PdfBuilder(file, backend);
	}

	public static PdfBuilder create(InputStream inputStream) {
		return new PdfBuilder(inputStream, new ApachePOIBackend());
	}

	public static PdfBuilder create(InputStream inputStream, Backend backend) {
		return new PdfBuilder(SourceDocx.of(inputStream), backend);
	}

	public static PdfBuilder create(byte[] bytes) {
		return new PdfBuilder(bytes, new ApachePOIBackend());
	}

	public static PdfBuilder create(byte[] bytes, Backend backend) {
		return new PdfBuilder(SourceDocx.of(bytes), backend);
	}

	private final SourceDocx inputDocx;
	private final Backend backend;

	public PdfBuilder(File file) {
		this(file, new ApachePOIBackend());
	}

	public PdfBuilder(File file, Backend backend) {
		this(SourceDocx.of(file), backend);
	}

	public PdfBuilder(InputStream inputStream) {
		this(inputStream, new ApachePOIBackend());
	}

	public PdfBuilder(InputStream inputStream, Backend backend) {
		this(SourceDocx.of(inputStream), backend);
	}

	public PdfBuilder(byte[] bytes) {
		this(bytes, new ApachePOIBackend());
	}

	public PdfBuilder(byte[] bytes, Backend backend) {
		this(SourceDocx.of(bytes), backend);
	}

	public PdfBuilder(SourceDocx inputDocx, Backend backend) {
		this.inputDocx = inputDocx;
		this.backend = requireNonNull(backend);
	}

	public void save(File destinationPDF) throws IOException {
		backend.save(inputDocx, destinationPDF);
	}

	public interface Backend {
		void save(SourceDocx inputDocx, File destinationPDF) throws IOException;
	}

	public static class ApachePOIBackend implements Backend {

		@Override
		public void save(SourceDocx inputDocx, File destinationPDF) throws IOException {
			try(OutputStream out = new BufferedOutputStream(new FileOutputStream(destinationPDF))) {
				try(InputStream doc = new BufferedInputStream(inputDocx.open())) {
					XWPFDocument document = new XWPFDocument(doc);
					PdfOptions options = PdfOptions.getDefault();
					PdfConverter.getInstance().convert(document, out, options);
				}
			}
		}
	}

	@FunctionalInterface
	public interface SourceDocx {

		InputStream open() throws IOException;

		static SourceDocx of(File file) {
			return () -> new FileInputStream(file);
		}

		static SourceDocx of(InputStream inputStream) {
			return () -> inputStream;
		}

		static SourceDocx of(byte[] bytes) {
			return () -> new ByteArrayInputStream(bytes);
		}
	}
}
