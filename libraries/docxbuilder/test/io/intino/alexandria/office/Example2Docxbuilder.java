package io.intino.alexandria.office;

import io.intino.alexandria.office.components.Image;
import io.intino.alexandria.office.components.ImageView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Example2Docxbuilder {

	public static void main(String[] args) throws IOException {
		new File("temp").mkdirs();

		DocxBuilder docx = DocxBuilder.create(new File("temp/ARML.docx"));

		docx.replace("header_value", "Header value");
		docx.replace("header_image", new ImageView(new Image(Files.readAllBytes(Paths.get("temp/image1.png")))));

		docx.save(new File("temp/example2.docx"));
	}
}
