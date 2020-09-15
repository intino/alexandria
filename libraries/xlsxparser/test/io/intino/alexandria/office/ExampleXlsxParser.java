package io.intino.alexandria.office;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static io.intino.alexandria.office.XlsxParser.*;

public class ExampleXlsxParser {

	public static void main(String[] args) throws IOException {
		Map<String, String> content = convert(new File("test-res/example.xlsx"));
		content.forEach((k, v) -> System.out.println("Content for sheet: " + k + "\n" + v));
	}
}
