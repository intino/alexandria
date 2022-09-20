package io.intino.alexandria.office;

import io.intino.alexandria.office.components.Alignment;
import io.intino.alexandria.office.components.Paragraph;
import io.intino.alexandria.office.components.StyleGroup;

import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import static io.intino.alexandria.office.components.Style.*;

public class Paragraphs_ {

	public static void main(String[] args) throws IOException {
		simpleTest();
		complexTest();
	}

	private static void complexTest() throws IOException {
		DocxBuilder db = DocxBuilder.create(new File("temp\\weekly-schedule.es.docx"));

		for(var day : DayOfWeek.values()) {
			List<String> theaters = List.of("Vencindario", "Los Alisios");
			List<String> orders = List.of("Orden 1", "Orden 2");

			List<Paragraph> paragraphs = new ArrayList<>();

			for(String theater : theaters) {

				Paragraph title = new Paragraph().text(theater).styles(new Bold(), new FontSize(16));
				Paragraph workOrders = new Paragraph().text(String.join("\n", orders)).withNoStyles();

				paragraphs.add(title);
				paragraphs.add(workOrders);
			}

			db.replace(day.name().toLowerCase() + "Theater", paragraphs);
		}

		db.save(new File("temp/agenda.docx"));
	}

	private static void simpleTest() throws IOException {
		DocxBuilder db = DocxBuilder.create(new File("temp\\Doc2.docx"));

		List<Paragraph> paragraphs = List.of(
				new Paragraph().text("Cine:").styles(Color.Red),
				new Paragraph().text("Los Alisios").alignment(Alignment.Center).styles(Color.Blue, new Bold(), new Underlined(), new FontSize(24))
		);

		db.replace("theater", paragraphs);
		db.replace("screen", new Paragraph("Sala 2"));

		db.save(new File("temp/Doc2-output.docx"));
	}
}
