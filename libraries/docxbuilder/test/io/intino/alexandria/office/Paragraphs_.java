package io.intino.alexandria.office;

import io.intino.alexandria.office.components.Alignment;
import io.intino.alexandria.office.components.Paragraph;

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

				Paragraph title = new Paragraph().text(theater, new Bold(), new FontSize(16)).br();

				Paragraph order = new Paragraph()
						.text("Sala 1", new Bold())
						.text(": ")
						.text(orders.get(0));

				paragraphs.add(title);
				paragraphs.add(order);
			}

			db.replace(day.name().toLowerCase() + "Theater", paragraphs);
		}

		db.save(new File("temp/agenda.docx"));
	}

	private static void simpleTest() throws IOException {
		DocxBuilder db = DocxBuilder.create(new File("temp\\Doc2.docx"));

		List<Paragraph> paragraphs = List.of(
				new Paragraph().text("Cine:", Color.Red),
				new Paragraph().text("Los Alisios", Color.Blue, new Bold(), new Underlined(), new FontSize(24)).alignment(Alignment.Center)
		);

		db.replace("theater", paragraphs);
		db.replace("screen", new Paragraph().text("Sala 2"));

		db.save(new File("temp/Doc2-output.docx"));
	}
}
