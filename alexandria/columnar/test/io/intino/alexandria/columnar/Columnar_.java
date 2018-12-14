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
	public void should_load_assa_from_one_zet() throws IOException {
		columnar.importColumn(new File("test-res/zets/Activos"));
	}

	@Test
	public void should_load_assa_from_multiple_zets() throws IOException {
		columnar.importColumn(new File("test-res/zets/Zona"));
	}

	@Test
	public void should_export_column_to_csv() throws IOException, ClassNotFoundException {
		columnar.select("Activos").from(new Timetag("201812")).to(new Timetag("201812")).intoCSV(new File(result, "Alta_201812.csv"));
	}

	@Test
	public void should_export_column_to_arff() throws IOException, ClassNotFoundException {
		columnar.select("Activos").from(new Timetag("201812")).to(new Timetag("201812")).intoARFF(new File(result, "Alta_201812.arff"), types());
	}

	@Test
	public void should_export_multivalued_column_zona_to_csv() throws IOException, ClassNotFoundException {
		columnar.select("Zona").from(new Timetag("201812")).to(new Timetag("201812")).intoCSV(new File(result, "Zona_201812.csv"));
	}

	@Test
	public void should_export_multivalued_column_to_arff() throws IOException, ClassNotFoundException {
		columnar.select("Zona").from(new Timetag("201812")).to(new Timetag("201812")).intoARFF(new File(result, "Zona_201812.arff"), types());
	}

	@Test
	public void should_build_assa_from_multiple_columns() throws IOException, ClassNotFoundException {
		columnar.select("Activos", "Zona").from(new Timetag("201812")).to(new Timetag("201812")).intoCSV(new File(result, "Mix_201812.csv"));
	}

	@Test
	public void should_export_filtered_column_to_csv() throws IOException, ClassNotFoundException {
		columnar.select("Activos", "Zona").from(new Timetag("201812")).to(new Timetag("201812")).filtered(l -> l == 1000100257L).intoCSV(new File(result, "Mix_filtered_201812.csv"));
	}

	private ColumnTypes types() {
		return new ColumnTypes().
				put("Activos", new Nominal(new String[]{"Singleton"})).
				put("Zona", new ColumnTypes.ColumnType.String());
	}
}
