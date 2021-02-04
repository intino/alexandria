package io.intino.test.schemas;

import io.intino.alexandria.led.Schema;
import io.intino.alexandria.led.buffers.store.ByteStore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.intino.alexandria.led.util.BitUtils.roundUp2;

public class TestSchema extends Schema {
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
	public static final int E_BITS = 64;
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

	public TestSchema(ByteStore store) {
		super(store);
	}

	@Override
	public long id() {
		return bitBuffer.getAlignedLong(0);
	}

	@Override
	public int size() {
		return SIZE;
	}

	public TestSchema id(long id) {
		bitBuffer.setAlignedLong(ID_OFFSET, id);
		return this;
	}

	public short a() {
		return bitBuffer.getShortNBits(A_OFFSET, A_BITS);
	}

	public TestSchema a(short a) {
		bitBuffer.setShortNBits(A_OFFSET, A_BITS, a);
		return this;
	}

	public int b() {
		return bitBuffer.getIntegerNBits(B_OFFSET, B_BITS);
	}

	public TestSchema b(int b) {
		bitBuffer.setIntegerNBits(B_OFFSET, B_BITS, b);
		return this;
	}

	public float c() {
		return bitBuffer.getAlignedReal32Bits(C_OFFSET);
	}

	public TestSchema c(float c) {
		bitBuffer.setAlignedReal32Bits(C_OFFSET, c);
		return this;
	}

	public int d() {
		return bitBuffer.getIntegerNBits(D_OFFSET, D_BITS);
	}

	public TestSchema d(int d) {
		bitBuffer.setIntegerNBits(D_OFFSET, D_BITS, d);
		return this;
	}

	public long e() {
		return bitBuffer.getLongNBits(E_OFFSET, E_BITS);
	}

	public TestSchema e(long e) {
		bitBuffer.setLongNBits(E_OFFSET, E_BITS, e);
		return this;
	}

	public double f() {
		return bitBuffer.getAlignedReal64Bits(F_OFFSET);
	}

	public TestSchema f(double f) {
		bitBuffer.setAlignedReal64Bits(F_OFFSET, f);
		return this;
	}

	public byte g() {
		return bitBuffer.getByteNBits(G_OFFSET, G_BITS);
	}

	public TestSchema g(byte g) {
		bitBuffer.setIntegerNBits(G_OFFSET, G_BITS, g);
		return this;
	}

	public SimpleWord h() {
		final byte word = bitBuffer.getByteNBits(H_OFFSET, H_BITS);
		return word == NULL ? null : SimpleWord.values()[word - 1];
	}

	public TestSchema h(SimpleWord h) {
		bitBuffer.setByteNBits(H_OFFSET, H_BITS, (byte)(h == null ? NULL : h.ordinal() + 1));
		return this;
	}

	public String i() {
		final int word = bitBuffer.getByteNBits(I_OFFSET, I_BITS);
		return word == NULL ? null : ResourceWord.values().get(word);
	}

	public TestSchema i(String i) {
		bitBuffer.setByteNBits(I_OFFSET, I_BITS, (byte)(i == null ? NULL : ResourceWord.indexOf(i)));
		return this;
	}

	public Boolean j() {
		final byte word = bitBuffer.getByteNBits(I_OFFSET, I_BITS);
		return word == NULL ? null : word == 1;
	}

	public TestSchema j(Boolean i) {
		bitBuffer.setByteNBits(J_OFFSET, J_BITS, (byte)(i == null ? NULL : i ? 1 : 2));
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
		if (!(obj instanceof TestSchema)) return false;
		TestSchema o = (TestSchema) obj;
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
