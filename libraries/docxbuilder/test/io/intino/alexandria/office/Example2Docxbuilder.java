package io.intino.alexandria.office;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Example2Docxbuilder {

	public static void main(String[] args) throws IOException, URISyntaxException {
		new File("temp").mkdirs();
		DocxBuilder.create(Path.of(Example2Docxbuilder.class.getResource("/example2.docx").toURI()).toFile())
				.replace("employee", "employee1")
				.replace("theater", "teatro")
				.replace("screen", "1")
				.replace("70a3eeeefbeb", Files.readAllBytes(Path.of("/Users/oroncal/Downloads/test.jpg")))
				.save(new File("temp/output2.docx"));
		System.out.println("Check output " + new File("temp/output.docx").getAbsolutePath());
	}
}
