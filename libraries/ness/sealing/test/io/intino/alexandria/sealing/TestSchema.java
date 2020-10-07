package io.intino.alexandria.sealing;

import io.intino.alexandria.led.Schema;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("IntegerDivisionInFloatingPointContext")
public class TestSchema implements Schema {
	private long id;
	private int i;
	private double d;

	public TestSchema() {
	}

	public TestSchema(long id, int i, double d) {
		this.id = id;
		this.i = i;
		this.d = d;
	}

	@Override
	public long id() {
		return id;
	}

	public int i() {
		return i;
	}

	public double d() {
		return d;
	}

	public static List<TestSchema> unsortedList() {
		List<TestSchema> result = new ArrayList<>();
		for (int i = 10; i <= 1000; i += 5) {
			int id = (int) Math.cos(i / 20 * Math.PI) * i;
			result.add(new TestSchema(id, i - 500, i * 100. / 20.));
		}
		return result;
	}

	public static List<TestSchema> anotherList() {
		List<TestSchema> result = new ArrayList<>();
		for (int i = 2000; i <= 3000; i += 5) {
			int id = (int) Math.cos(i / 20 * Math.PI) * i;
			result.add(new TestSchema(id, i - 500, i * 100. / 20.));
		}
		return result;
	}
}
