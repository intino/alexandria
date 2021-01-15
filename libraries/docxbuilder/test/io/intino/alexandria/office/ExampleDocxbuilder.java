package io.intino.alexandria.office;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;

public class ExampleDocxbuilder {

	public static void main(String[] args) throws IOException, URISyntaxException {
		new File("temp").mkdirs();
		DocxBuilder.create(Path.of(ExampleDocxbuilder.class.getResource("/example.docx").toURI()).toFile())
				.replace("doctype", "example")
				.replace("date", LocalDate.now().toString())
				.replace("os", System.getProperty("os.name"))
				.table("tabla1")
					.addRow(new HashMap<>() {{ put("campo1", "valor campo 1.1"); put("campo2", "valor campo 2.1");}})
					.addRow(new HashMap<>() {{ put("campo1", "valor campo 1.2"); put("campo2", "valor campo 2.2");}})
					.end()
				.save(new File("temp/output.docx"));
		System.out.println("Check output " + new File("temp/output.docx").getAbsolutePath());
	}
}
