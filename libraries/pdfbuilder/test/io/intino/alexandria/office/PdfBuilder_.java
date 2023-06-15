package io.intino.alexandria.office;

import io.intino.alexandria.office.PdfBuilder.ApachePOIBackend;

import java.io.File;

public class PdfBuilder_ {

	public static void main(String[] args) throws Exception {
		File inputDocx = new File("temp/agenda.docx");
		File pdf1 = new File("temp/agenda.pdf");

		PdfBuilder.create(inputDocx, new ApachePOIBackend()).save(pdf1);
	}
}
