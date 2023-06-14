package io.intino.alexandria.office;

import io.intino.alexandria.office.PdfBuilder.ApachePOIBackend;
import io.intino.alexandria.office.PdfBuilder.Docx4jBackend;

import java.io.File;

public class PdfBuilder_ {

	public static void main(String[] args) throws Exception {
		File inputDocx = new File("temp/20230614-agenda.docx");
		File pdf1 = new File("temp/20230614-agenda-1.pdf");
		File pdf2 = new File("temp/20230614-agenda-2.pdf");

		PdfBuilder.create(inputDocx, new ApachePOIBackend()).save(pdf1);
		PdfBuilder.create(inputDocx, new Docx4jBackend()).save(pdf2);
	}
}
