package io.intino.alexandria.office;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.*;

import static java.util.Objects.requireNonNull;

public class PdfBuilder {

	public static PdfBuilder create(File inputDocx) {
		return new PdfBuilder(inputDocx, new Docx4jBackend());
	}

	public static PdfBuilder create(File inputDocx, Backend backend) {
		return new PdfBuilder(inputDocx, backend);
	}

	private final File inputDocx;
	private final Backend backend;

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
					PdfOptions options = PdfOptions.create();
					PdfConverter.getInstance().convert(document, out, options);
				}
			}
		}
	}

	public static class Docx4jBackend implements Backend {

		@Override
		public void save(File inputDocx, File destinationPDF) throws IOException {
			try(OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destinationPDF))) {
				Docx4J.toPDF(readDocx(inputDocx), outputStream);
			} catch (Docx4JException e) {
				throw new IOException(e);
			}
		}

		private WordprocessingMLPackage readDocx(File inputDocx) throws IOException {
			try(InputStream inputStream = new FileInputStream(inputDocx)) {
				return WordprocessingMLPackage.load(inputStream);
			} catch (Docx4JException e) {
				throw new IOException(e);
			}
		}
	}
}
