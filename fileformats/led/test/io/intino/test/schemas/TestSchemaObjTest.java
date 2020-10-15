package io.intino.test.schemas;

import io.intino.alexandria.led.allocators.DefaultAllocator;
import io.intino.alexandria.led.allocators.SchemaAllocator;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static io.intino.alexandria.led.util.BitUtils.maxPossibleNumber;
import static io.intino.alexandria.led.util.MemoryUtils.memset;
import static io.intino.test.schemas.TestSchemaObj.*;
import static org.junit.Assert.assertEquals;

public class TestSchemaObjTest {
	private final Random random;
	private final SchemaAllocator<TestSchemaObj> allocator;
	private TestSchemaObj schema;

	public TestSchemaObjTest() {
		random = new Random();
		allocator = new DefaultAllocator<>(SIZE, TestSchemaObj::new);
	}

	@Before
	public void setup() {
		schema = allocator.malloc();
		// Random values in memory
		for (int i = 0; i < SIZE; i++) {
			memset(schema.address(), 1, random.nextInt() - random.nextInt());
		}
	}

	@Test
	public void id() {

		final long id = random.nextLong();

		schema.id(id);

		assertEquals(id, schema.id());
	}

	@Test
	public void a() {

		short a = (short) maxPossibleNumber(A_BITS);

		schema.a(a);

		assertEquals(a, schema.a());
	}

	@Test
	public void b() {

		int b = (int) maxPossibleNumber(B_BITS);

		schema.b(b);

		assertEquals(b, schema.b());
	}

	@Test
	public void c() {

		float c = (float) maxPossibleNumber(C_BITS);

		schema.c(c);

		assertEquals(c, schema.c(), 0.0f);
	}

	@Test
	public void d() {

		int d = (int) maxPossibleNumber(D_BITS);

		schema.d(d);

		assertEquals(d, schema.d());
	}

	@Test
	public void e() {

		long e = (long) maxPossibleNumber(E_BITS);

		schema.e(e);

		assertEquals(schema.toString(), e, schema.e());
	}

	@Test
	public void f() {

		double f = maxPossibleNumber(F_BITS);

		schema.f(f);

		assertEquals(f, schema.f(), 0.0);
	}

	@Test
	public void g() {

		byte g = (byte) maxPossibleNumber(G_BITS);

		schema.g(g);

		assertEquals(g, schema.g());
	}


	private int factorial(int n) {
		int result = 1;
		int i = n;
		while (i > 1) {
			result *= i;
			--i;
		}
		return result;
	}
}