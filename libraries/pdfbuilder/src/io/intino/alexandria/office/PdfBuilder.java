package io.intino.alexandria.office;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;

public class PdfBuilder {

	public static PdfBuilder create(File inputDocx) {
		return new PdfBuilder(inputDocx);
	}

	private final File inputDocx;

	public PdfBuilder(File inputDocx) {
		this.inputDocx = inputDocx;
	}

	public void save(File file) throws IOException {
		try(OutputStream out = new FileOutputStream(file)) {
			try(InputStream doc = new FileInputStream(inputDocx)) {
				XWPFDocument document = new XWPFDocument(doc);
				PdfOptions options = PdfOptions.create();
				PdfConverter.getInstance().convert(document, out, options);
			}
		}
	}
}
