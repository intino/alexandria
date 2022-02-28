package io.test;

import io.intino.alexandria.sumus.Cube;
import io.intino.alexandria.sumus.Dimension;
import io.intino.alexandria.sumus.Ledger;
import io.intino.alexandria.sumus.Slice;
import io.intino.alexandria.sumus.ledgers.columnar.ColumnarLedger;
import io.intino.alexandria.sumus.parser.LedgerDefinition;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
	public static void main(String[] args) throws IOException {
		LedgerDefinition definition = LedgerDefinition.read(new File("ledgers/distritos.ledger"));
		Ledger ledger = new ColumnarLedger(definition).read(new File("ledgers/distritos/202201.tsv"),"\t");
		Dimension area = ledger.dimension("lugar");
		System.out.println(area.levels());
		List<Slice> slices = area.slices(2);
		for (Slice slice : slices) {
			System.out.println(slice);
		}


	}


	public static void maidn(String[] args) throws IOException {
		LedgerDefinition definition = LedgerDefinition.read(new File("ledgers/devices.ledger"));
		Ledger ledger = new ColumnarLedger(definition).read(new File("ledgers/devices/20220227.tsv"),"\t");

		Dimension type = ledger.dimension("type");
		Dimension make = ledger.dimension("make");
		Dimension area = ledger.dimension("area");

		Slice projector = type.slice("projector");
		Slice player = type.slice("player");
		Slice mexico = area.slice("mexico");

		Cube projectorsByMake = ledger.cube()
				.filter(projector, mexico)
				.dimensions(make)
				.build();
		print(projectorsByMake);

		Cube playersByMake = ledger.cube()
				.filter(player, mexico)
				.dimensions(make)
				.build();
		print(playersByMake);

		Cube playerByArea = ledger.cube()
				.filter(player)
				.dimensions(area)
				.build();
		print(playerByArea);

	}

	private static void print(Cube cube) {
		for (Cube.Cell cell : cube.cells()) {
			int value = (int) cell.indicator("_count(theater)").value();
			if (value == 0) continue;
			System.out.println(translate(str(cell.slices())) + ": " + value);
		}
		System.out.println();
	}

	private static final Map<String,String> labels = new HashMap<>() {{
		put("","TOTAL");
		put("NA","Offline");
	}};
	private static String translate(String str) {
		return labels.getOrDefault(str,str);
	}

	private static String str(List<?> slices) {
		return slices.stream().map(Object::toString).collect(Collectors.joining(","));
	}
}
