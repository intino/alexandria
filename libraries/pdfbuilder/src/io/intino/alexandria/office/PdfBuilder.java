package io.intino.alexandria.office;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;

import static java.util.Objects.requireNonNull;

public class PdfBuilder {

	public static PdfBuilder create(File inputDocx) {
		return new PdfBuilder(inputDocx, new ApachePOIBackend());
	}

	public static PdfBuilder create(File inputDocx, Backend backend) {
		return new PdfBuilder(inputDocx, backend);
	}

	private final File inputDocx;
	private final Backend backend;

	public PdfBuilder(File inputDocx) {
		this(inputDocx, new ApachePOIBackend());
	}

	public PdfBuilder(File inputDocx, Backend backend) {
		this.inputDocx = inputDocx;
		this.backend = requireNonNull(backend);
	}

	public void save(File destinationPDF) throws IOException {
		backend.save(inputDocx, destinationPDF);
	}

	public interface Backend {
		void save(File inputDocx, File destinationPDF) throws IOException;
	}

	public static class ApachePOIBackend implements Backend {

		@Override
		public void save(File inputDocx, File destinationPDF) throws IOException {
			try(OutputStream out = new BufferedOutputStream(new FileOutputStream(destinationPDF))) {
				try(InputStream doc = new BufferedInputStream(new FileInputStream(inputDocx))) {
					XWPFDocument document = new XWPFDocument(doc);
					PdfOptions options = PdfOptions.getDefault();
					PdfConverter.getInstance().convert(document, out, options);
				}
			}
		}
	}
}
