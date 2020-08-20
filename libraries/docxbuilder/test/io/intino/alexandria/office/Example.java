package io.intino.alexandria.office;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class Example {

	public static void main(String[] args) throws IOException {
		new File("temp").mkdirs();
		DocxBuilder.create(new File("test-res/example.docx"))
				.replace("doctype", "example")
				.replace("date", LocalDate.now().toString())
				.replace("os", System.getProperty("os.name"))
				.save(new File("temp/output.docx"));
		System.out.println("Check output " + new File("temp/output.docx").getAbsolutePath());
	}
}
