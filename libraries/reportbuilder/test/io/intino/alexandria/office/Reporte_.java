package io.intino.alexandria.office;

import org.junit.Test;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;

public class Reporte_ {

	@Test
	public void should_generate_html() throws URISyntaxException, IOException {
		File file = new File(Reporte_.class.getResource("/example2.tsv").toURI());
		File result = new File("/tmp/out.html");
		ByteArrayInputStream data = (ByteArrayInputStream) HtmlReportBuilder.create().title("Ejemplo").build(file);
		Files.write(result.toPath(), data.readAllBytes());
		Desktop.getDesktop().browse(result.toURI());
	}

	@Test
	public void should_generate_excel() throws URISyntaxException, IOException {
		File file = new File(Reporte_.class.getResource("/example2.tsv").toURI());
		File template = new File(Reporte_.class.getResource("/reporte.xlsx").toURI());
		File result = new File("/tmp/out.xlsx");
		ByteArrayInputStream data = (ByteArrayInputStream) ExcelReportBuilder.create().template(template).title("Ejemplo").build(file);
		Files.write(result.toPath(), data.readAllBytes());
	}
}
