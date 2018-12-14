package io.intino.alexandria.columnar;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.columnar.ColumnTypes.ColumnType.Nominal;
import io.intino.alexandria.zet.Zet;
import io.intino.alexandria.zet.ZetReader;
import io.intino.alexandria.zet.ZetStream;
import io.intino.alexandria.zet.ZetWriter;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static java.lang.Long.parseLong;

public class Columnar_ {
	private Columnar columnar;
	private File result;

	@Before
	public void setUp() {
		File columnarFile = new File("temp/columnar");
		columnarFile.mkdirs();
		columnar = new Columnar(columnarFile);
		result = new File("temp/exports");
		result.mkdirs();
	}

	@Test
	public void should_load_assa_from_one_zet() throws IOException {
		columnar.importColumn(new File("test-res/zets/Alta"));
	}

	@Test
	public void should_load_assa_from_multiple_zets() throws IOException {
//		columnar.importColumn(new File("test-res/zets/Potencia"));
//		columnar.importColumn(new File("test-res/zets/Zona"));
		Zet zet = new Zet(new ZetStream.Intersection(
				new ZetReader(new File("test-res/zets/Potencia/201810/4.zet")),
				new ZetReader(new File("test-res/zets/Potencia/201810/5.zet"))));
		for (int i = 0; i < zet.ids().length; i++) {
			System.out.println(zet.ids()[i]);
		}
		System.out.println(zet.size());
	}

	@Test
	public void should_export_column_to_csv() throws IOException, ClassNotFoundException {
		columnar.select("Alta").from(new Timetag("201810")).to(new Timetag("201810")).intoCSV(new File(result, "Alta_201810.csv"));
	}

	@Test
	public void should_export_column_to_arff() throws IOException, ClassNotFoundException {
		columnar.select("Alta").from(new Timetag("201810")).to(new Timetag("201810")).intoARFF(new File(result, "Alta_201810.arff"), types());
	}

	@Test
	public void should_export_multivalued_column_zona_to_csv() throws IOException, ClassNotFoundException {
		columnar.select("Zona").from(new Timetag("201810")).to(new Timetag("201810")).intoCSV(new File(result, "Zona_201810.csv"));
	}

	@Test
	public void should_export_multivalued_column_potencia_to_csv() throws IOException, ClassNotFoundException {
		columnar.select("Potencia").from(new Timetag("201810")).to(new Timetag("201810")).intoCSV(new File(result, "Potencia_201810.csv"));
	}

	@Test
	public void should_export_multivalued_column_to_arff() throws IOException, ClassNotFoundException {
		columnar.select("B").from(new Timetag("201810")).to(new Timetag("201810")).intoARFF(new File(result, "B_201810.arff"), types());
	}

	@Test
	public void should_build_assa_from_multiple_onevalue_columns() throws IOException, ClassNotFoundException {
		columnar.select("A", "C").from(new Timetag("201810")).to(new Timetag("201810")).intoCSV(new File(result, "AC_201810.csv"));
		columnar.select("E", "F").from(new Timetag("201810")).to(new Timetag("201810")).intoCSV(new File(result, "EF_201810.csv"));
	}

	@Test
	public void should_export_multiple_multivalued_columns() throws IOException, ClassNotFoundException {
		columnar.select("B", "D").from(new Timetag("201810")).to(new Timetag("201810")).intoCSV(new File(result, "BD_201810.csv"));
		columnar.select("B", "D").from(new Timetag("201810")).to(new Timetag("201810")).intoARFF(new File(result, "BD_201810.arff"), types());
		columnar.select("B", "E", "F").from(new Timetag("201810")).to(new Timetag("201810")).intoARFF(new File(result, "BEF_201810.arff"), types());
	}

	@Test
	public void should_export_filtered_column_to_csv() throws IOException, ClassNotFoundException {
		columnar.select("A").from(new Timetag("201810")).to(new Timetag("201810")).filtered(id -> id == 264100603869L).intoCSV(new File(result, "A_filtered_201810.csv"));
		columnar.select("A").from(new Timetag("201810")).to(new Timetag("201810")).filtered(id -> id >= 264100701879L).intoCSV(new File(result, "A_filtered2_201810.csv"));
		columnar.select("A", "C").from(new Timetag("201810")).to(new Timetag("201810")).filtered(id -> id >= 264100701879L).intoCSV(new File(result, "AC_filtered_201810.csv"));
	}

	private ColumnTypes types() {
		return new ColumnTypes().
				put("Alta", new Nominal(new String[]{"Alta"})).
				put("B", new Nominal(new String[]{"one", "two"})).
				put("D", new Nominal(new String[]{"DL10A", "DL10B"})).
				put("E", new Nominal(new String[]{"Chrome"})).
				put("F", new Nominal(new String[]{"Safary"}));
	}
}
