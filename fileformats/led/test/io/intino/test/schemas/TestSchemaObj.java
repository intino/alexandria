package io.intino.test.schemas;

import io.intino.alexandria.led.Schema;
import io.intino.alexandria.led.buffers.store.ByteStore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.intino.alexandria.led.util.BitUtils.byteIndex;
import static io.intino.alexandria.led.util.BitUtils.roundUp2;

public class TestSchemaObj extends Schema {
	public enum SimpleWord {
		A(1), B(2), C(1);
		int value;

		SimpleWord(int value) {
			this.value = value;
		}

		public int value() {
			return value;
		}
	}

	public static final int ID_OFFSET = 0;
	public static final int ID_BITS = Long.SIZE;
	public static final int A_OFFSET = ID_OFFSET + ID_BITS;
	public static final int A_BITS = 10;
	public static final int B_OFFSET = A_OFFSET + A_BITS;
	public static final int B_BITS = Integer.SIZE;
	public static final int C_OFFSET = roundUp2(B_OFFSET + B_BITS, Float.SIZE);
	public static final int C_BITS = Float.SIZE;
	public static final int D_OFFSET = C_OFFSET + C_BITS;
	public static final int D_BITS = 29;
	public static final int E_OFFSET = D_OFFSET + D_BITS;
	public static final int E_BITS = 55;
	public static final int F_OFFSET = roundUp2(E_OFFSET + E_BITS, Double.SIZE);
	public static final int F_BITS = Double.SIZE;
	public static final int G_OFFSET = F_OFFSET + F_BITS;
	public static final int G_BITS = 4;
	public static final int H_OFFSET = G_OFFSET + G_BITS;
	public static final int H_BITS = 4;
	public static final int I_OFFSET = H_OFFSET + H_BITS;
	public static final int I_BITS = 4;
	public static final int J_OFFSET = I_OFFSET + I_BITS;
	public static final int J_BITS = 2;
	// private long id;
	// private short a;
	// private int b;
	// private float c;
	// private int d;
	// private long e;
	// private double f;
	// private byte g;
	public static final int SIZE = (int) Math.ceil((I_OFFSET + I_BITS) / (float) Byte.SIZE);

	public TestSchemaObj(ByteStore store) {
		super(store);
	}

	@Override
	public long id() {
		return getAlignedLong(0);
	}

	@Override
	public int size() {
		return SIZE;
	}

	public TestSchemaObj id(long id) {
		setAlignedLong(ID_OFFSET, id);
		return this;
	}

	public short a() {
		return (short) getInteger(A_OFFSET, A_BITS);
	}

	public TestSchemaObj a(short a) {
		setInteger(A_OFFSET, A_BITS, a);
		return this;
	}

	public int b() {
		return (int) getInteger(B_OFFSET, B_BITS);
	}

	public TestSchemaObj b(int b) {
		setInteger(B_OFFSET, B_BITS, b);
		return this;
	}

	public float c() {
		return getAlignedFloat(byteIndex(C_OFFSET));
	}

	public TestSchemaObj c(float c) {
		setAlignedFloat(byteIndex(C_OFFSET), c);
		return this;
	}

	public int d() {
		return (int) getInteger(D_OFFSET, D_BITS);
	}

	public TestSchemaObj d(int d) {
		setInteger(D_OFFSET, D_BITS, d);
		return this;
	}

	public long e() {
		return getInteger(E_OFFSET, E_BITS);
	}

	public TestSchemaObj e(long e) {
		setInteger(E_OFFSET, E_BITS, e);
		return this;
	}

	public double f() {
		return getAlignedDouble(byteIndex(F_OFFSET));
	}

	public TestSchemaObj f(double f) {
		setAlignedDouble(byteIndex(F_OFFSET), f);
		return this;
	}

	public byte g() {
		return (byte) getInteger(G_OFFSET, G_BITS);
	}

	public TestSchemaObj g(byte g) {
		setInteger(G_OFFSET, G_BITS, g);
		return this;
	}

	public SimpleWord h() {
		final int word = (int) getInteger(H_OFFSET, H_BITS);
		return word == NULL ? null : SimpleWord.values()[word - 1];
	}

	public TestSchemaObj h(SimpleWord h) {
		setInteger(H_OFFSET, H_BITS, h == null ? NULL : h.ordinal() + 1);
		return this;
	}

	public String i() {
		final int word = (int) getInteger(I_OFFSET, I_BITS);
		return word == NULL ? null : ResourceWord.values().get(word);
	}

	public TestSchemaObj i(String i) {
		setInteger(I_OFFSET, I_BITS, i == null ? NULL : ResourceWord.indexOf(i));
		return this;
	}

	public Boolean j() {
		final int word = (int) getInteger(I_OFFSET, I_BITS);
		return word == NULL ? null : word == 1;
	}

	public TestSchemaObj j(Boolean i) {
		setInteger(J_OFFSET, J_BITS, i == null ? NULL : i ? 1 : 2);
		return this;
	}

	@Override
	public String toString() {
		return "TestSchemaObj{"
				+ "id = " + id()
				+ ", a = " + a()
				+ ", b = " + b()
				+ ", c = " + c()
				+ ", d = " + d()
				+ ", e = " + e()
				+ ", f = " + f()
				+ ", g = " + g()
				+ "}";
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id());
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TestSchemaObj)) return false;
		TestSchemaObj o = (TestSchemaObj) obj;
		return id() == o.id()
				&& a() == o.a()
				&& b() == o.b()
				&& c() == o.c()
				&& d() == o.d()
				&& e() == o.e()
				&& f() == o.f()
				&& g() == o.g();
	}

	public static class ResourceWord {
		private static final Map<Integer, String> values;

		static {
			values = new BufferedReader(new InputStreamReader(ResourceWord.class.getResourceAsStream("ResourceWord.tsv"))).lines().map(l -> l.split("\t")).collect(Collectors.toMap(l -> Integer.parseInt(l[0]), l -> l[1]));
		}

		public static Map<Integer, String> values() {
			return values;
		}

		public static long indexOf(String i) {
			Map.Entry<Integer, String> e = values.entrySet().stream().filter(en -> en.getValue().equals(i)).findFirst().orElse(null);
			return e == null ? NULL : e.getKey();
		}
	}
}
