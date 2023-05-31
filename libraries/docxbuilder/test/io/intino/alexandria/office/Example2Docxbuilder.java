package io.intino.alexandria.office;

import io.intino.alexandria.office.components.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Example2Docxbuilder {

	public static void main(String[] args) throws IOException {
		new File("temp").mkdirs();

		DocxBuilder docx = DocxBuilder.create(new File("temp/ARML.docx"));

		ImageView imageView = new ImageView(new Image(new File("temp/image1.png")));

		imageView.widthWrapping(ImageView.WrapOption.ClampToPage); // Permite que la imagen haga resize hasta lo que permita la pagina
		imageView.heightWrapping(ImageView.WrapOption.ClampToTemplate); // Fija la altura a la imagen en el template
		imageView.keepAspectRatio(true);

		docx.replace("my_image_field", imageView);

		docx.replace("header_value", "Header & value");
		docx.replace("header_image", new ImageView(new Image(Files.readAllBytes(Paths.get("temp/image1.png")))));

		docx.replace("theater", "Auditoria P&S");
		docx.replace("area", new Paragraph().text(new Text("Auditoria P&S").styles(new Style.Bold())));
		docx.replace("screen", new Paragraph().text(new Text("Auditoria P&S")));

		File file = new File("temp/example__2.docx");
		docx.save(file);

		System.out.println(file.getAbsolutePath());
	}
}
