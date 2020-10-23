package io.intino.alexandria.sealing;

import io.intino.alexandria.led.Transaction;
import io.intino.alexandria.led.allocators.TransactionAllocator;
import io.intino.alexandria.led.allocators.stack.StackAllocators;
import io.intino.alexandria.led.buffers.store.ByteStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static io.intino.alexandria.led.util.BitUtils.roundUp2;

public class TestSchema extends Transaction {
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
		bitBuffer.setByteNBits(G_OFFSET, G_BITS, g);
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
		if (!(obj instanceof TestSchema)) {
			return false;
		}
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

	public static List<TestSchema> unsortedList() {
		TransactionAllocator<TestSchema> allocator = StackAllocators.newManaged(SIZE, 1000, TestSchema::new);
		List<TestSchema> result = new ArrayList<>();
		for (int i = 10; i <= 1000; i += 5) {
			int id = (int) Math.cos(i / 20 * Math.PI) * i;
			result.add(allocator.calloc().id(id).b(i - 500).f(i * 100.0 / 20.0));
		}
		return result;
	}

	public static List<TestSchema> anotherList() {
		TransactionAllocator<TestSchema> allocator = StackAllocators.newManaged(SIZE, 3000, TestSchema::new);
		List<TestSchema> result = new ArrayList<>();
		for (int i = 2000; i <= 3000; i += 5) {
			int id = (int) Math.cos(i / 20 * Math.PI) * i;
			result.add(allocator.calloc().id(id).b(i - 500).f(i * 100.0 / 20.0));
		}
		return result;
	}
}
