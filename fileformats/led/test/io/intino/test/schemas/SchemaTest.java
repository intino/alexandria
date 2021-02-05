package io.intino.test.schemas;

import io.intino.alexandria.led.allocators.DefaultAllocator;
import io.intino.alexandria.led.allocators.SchemaAllocator;
import io.intino.alexandria.led.util.memory.LedLibraryConfig;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static io.intino.alexandria.led.util.BitUtils.maxPossibleNumber;
import static io.intino.alexandria.led.util.memory.MemoryUtils.memset;
import static io.intino.test.schemas.TestSchema.*;
import static org.junit.Assert.assertEquals;

@Ignore
public class SchemaTest {

	static {
		//LedLibraryConfig.USE_MEMORY_TRACKER.set(true);
		//LedLibraryConfig.ALLOCATION_CALLBACK.set(alloc -> System.out.println(alloc.toStringPretty()));
		//LedLibraryConfig.DEFAULT_BYTE_ORDER.set(ByteOrder.BIG_ENDIAN);
	}

	private final Random random;
	private final SchemaAllocator<TestSchema> allocator;
	private TestSchema schema;

	public SchemaTest() {
		random = new Random();
		allocator = new DefaultAllocator<>(SIZE, TestSchema::new);
	}

	@Before
	public void setup() {
		schema = allocator.malloc();
		// Random values in memory
		for (int i = 0; i < SIZE; i++) memset(schema.address(), 1, random.nextInt() - random.nextInt());
	}

	@Ignore
	@Test
	public void testSetFieldDoesNotModifyOtherFields() {
		schema = allocator.calloc();
		List<Function<TestSchema, Number>> getters = getters();
		List<BiConsumer<TestSchema, Number>> setters = setters();
		List<Function<Number, Number>> interpreter = interpreters();
		Number[] lastValues = new Number[getters.size()];
		Random random = new Random(System.nanoTime());

		for(int i = 0;i < setters.size();i++) {
			BiConsumer<TestSchema, Number> setter = setters.get(i);
			final Function<TestSchema, Number> getter = getters.get(i);
			final Function<Number, Number> interpret = interpreter.get(i);
			Number value = interpret.apply(random.nextInt());
			setter.accept(schema, value);
			// Assert the value is correctly set
			assertEquals(value, getter.apply(schema));
			lastValues[i] = value;
			// Check if other fields have been affected
			for(int j = 0;j < getters.size();j++) {
				if(i == j) continue;
				if(lastValues[j] == null) continue;
				assertEquals(interpreter.get(j).apply(lastValues[j]), getters.get(j).apply(schema));
			}
			System.out.println(schema);
		}
	}

	private List<Function<Number, Number>> interpreters() {
		return List.of(
				Number::longValue,
				Number::shortValue,
				Number::intValue,
				Number::floatValue,
				Number::intValue,
				Number::longValue,
				Number::doubleValue,
				Number::intValue
		);
	}

	private List<Function<TestSchema, Number>> getters() {
		return List.of(
				TestSchema::id,
				TestSchema::a,
				TestSchema::b,
				TestSchema::c,
				TestSchema::d,
				TestSchema::e,
				TestSchema::f,
				TestSchema::g
		);
	}

	private List<BiConsumer<TestSchema, Number>> setters() {
		return List.of(
				TestSchema::id,
				TestSchema::a,
				TestSchema::b,
				TestSchema::c,
				TestSchema::d,
				TestSchema::e,
				TestSchema::f,
				TestSchema::g
		);
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
		schema = allocator.calloc();
		long e = (long) maxPossibleNumber(E_BITS) / 2;
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