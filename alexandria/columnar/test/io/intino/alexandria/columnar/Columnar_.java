package io.intino.alexandria.columnar;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.columnar.ColumnTypes.ColumnType.Nominal;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

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
	public void should_load_assa_from_several_zets() throws IOException {
		columnar.load("Adscripcion.Ruta").from(new File("datalake/sets/Adscripcion.Ruta"));
	}

	@Test
	public void should_load_assa_from_one_zet() throws IOException {
		columnar.load("A").from(new File("test-res/zets/A"));
		columnar.load("C").from(new File("test-res/zets/C"));
		columnar.load("E").from(new File("test-res/zets/E"));
		columnar.load("F").from(new File("test-res/zets/F"));
	}

	@Test
	public void should_load_assa_from_multiple_zets() throws IOException {
		columnar.load("B").from(new File("test-res/zets/B"));
		columnar.load("D").from(new File("test-res/zets/D"));
	}

	@Test
	public void should_export_column_to_csv() throws IOException, ClassNotFoundException {
		columnar.select("A").from(new Timetag("201810")).to(new Timetag("201810")).intoCSV(new File(result, "A_201810.csv"));
	}

	@Test
	public void should_export_column_to_arff() throws IOException, ClassNotFoundException {
		columnar.select("A").from(new Timetag("201810")).to(new Timetag("201810")).intoARFF(new File(result, "A_201810.arff"), types());
	}

	@Test
	public void should_export_multivalued_column_to_csv() throws IOException, ClassNotFoundException {
		columnar.select("B").from(new Timetag("201810")).to(new Timetag("201810")).intoCSV(new File(result, "B_201810.csv"));
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
				put("A", new Nominal(new String[]{"one", "two"})).
				put("B", new Nominal(new String[]{"one", "two"})).
				put("D", new Nominal(new String[]{"DL10A", "DL10B"})).
				put("E", new Nominal(new String[]{"Chrome"})).
				put("F", new Nominal(new String[]{"Safary"}));
	}
}
