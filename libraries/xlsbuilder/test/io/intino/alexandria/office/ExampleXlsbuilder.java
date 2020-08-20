package io.intino.alexandria.office;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ExampleXlsbuilder {

	public static void main(String[] args) throws IOException {
		new File("temp").mkdirs();
		File csv = new File("temp/example.csv");
		String ending = Files.readString(new File("test-res/ending.csv").toPath());
		Files.write(csv.toPath(), ("\n\n\n\n$id;$name;$address;$municipality;#amount\n" +
				"Id1;Company1;Address1;Municipality1;30.23\n" +
				"Id2;Company2;Address2;Municipality2;3034242.23\n" +
				"Id3;Company3;Address3;Municipality3;23123.23\n" + ending).getBytes());
		XlsBuilder.create().append("sheet1", csv).save(new File("temp/result.xls"));
		System.out.println("Check output: " + new File("temp/result.xlsx").getAbsolutePath());
	}
}
