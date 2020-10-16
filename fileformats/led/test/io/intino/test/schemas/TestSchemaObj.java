package io.intino.test.schemas;

import io.intino.alexandria.led.Schema;
import io.intino.alexandria.led.buffers.store.ByteStore;

import java.util.Objects;

import static io.intino.alexandria.led.util.BitUtils.byteIndex;
import static io.intino.alexandria.led.util.BitUtils.roundUp2;

public class TestSchemaObj extends Schema {
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

	public static final int SIZE = (int) Math.ceil((G_OFFSET + G_BITS) / (float) Byte.SIZE);

	// private long id;
	// private short a;
	// private int b;
	// private float c;
	// private int d;
	// private long e;
	// private double f;
	// private byte g;

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
		if (!(obj instanceof TestSchemaObj)) {
			return false;
		}
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
}
