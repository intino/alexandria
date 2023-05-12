package io.intino.alexandria.office;

import java.io.File;
import java.io.IOException;

public class PdfBuilder_ {

	public static void main(String[] args) throws IOException {
		PdfBuilder.create(new File("temp/agenda.docx")).save(new File("temp/pdf222.pdf"));
	}
}
