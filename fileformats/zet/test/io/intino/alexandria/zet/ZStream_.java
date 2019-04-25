package io.intino.alexandria.zet;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;

import static junit.framework.TestCase.*;

public class ZStream_ {

	private ByteArrayOutputStream stream;

	private static void eof(ZInputStream input) throws IOException {
		try {
			input.readLong();
			fail("EOF not caught");
		} catch (EOFException e) {
			assertTrue("EOF caught", true);
		}
	}

	@Before
	public void setUp() throws Exception {
		stream = new ByteArrayOutputStream();
	}

	@Test
	public void should_work_with_an_empty_zet() throws IOException {
		ZOutputStream stream = new ZOutputStream(this.stream);
		stream.close();
		byte[] bytes = bytes();
		assertEquals(16, bytes.length);
		assertEquals(0xFFFFFFFFFFFFFFFFL, getLong(bytes, 0));
		assertEquals(0x0L, getLong(bytes, 8));

		ZInputStream input = new ZInputStream(new ByteArrayInputStream(bytes));
		eof(input);
	}

	@Test
	public void should_work_with_a_zet_with_one_element() throws IOException {
		ZOutputStream stream = new ZOutputStream(this.stream);
		stream.writeLong(0);
		stream.close();
		byte[] bytes = bytes();
		assertEquals(19, bytes.length);
		assertEquals(0, bytes[0]);
		assertEquals(1, bytes[1]);
		assertEquals(0, bytes[2]);
		assertEquals(0xFFFFFFFFFFFFFFFFL, getLong(bytes, 3));
		assertEquals(0x1L, getLong(bytes, 11));

		ZInputStream input = new ZInputStream(new ByteArrayInputStream(bytes));
		assertEquals(0, input.readLong());
		eof(input);
	}

	@Test
	public void should_work_with_a_zet_with_one_element_of_level_1() throws IOException {
		ZOutputStream stream = new ZOutputStream(this.stream);
		stream.writeLong(0x433);
		stream.close();
		byte[] bytes = bytes();
		assertEquals(20, bytes.length);
		assertEquals(1, bytes[0]);
		assertEquals(4, bytes[1]);
		assertEquals(1, bytes[2]);
		assertEquals(0x33, bytes[3]);
		assertEquals(0xFFFFFFFFFFFFFFFFL, getLong(bytes, 4));
		assertEquals(0x1L, getLong(bytes, 12));

		ZInputStream input = new ZInputStream(new ByteArrayInputStream(bytes));
		assertEquals(0x433, input.readLong());
		eof(input);
	}

	@Test
	public void should_work_with_a_zet_with_one_element_of_level_2() throws IOException {
		ZOutputStream stream = new ZOutputStream(this.stream);
		stream.writeLong(0xAB433);
		stream.close();
		byte[] bytes = bytes();
		assertEquals(21, bytes.length);
		assertEquals(2, bytes[0]);
		assertEquals(0xA, bytes[1]);
		assertEquals((byte) 0xB4, bytes[2]);
		assertEquals(1, bytes[3]);
		assertEquals(0x33, bytes[4]);
		assertEquals(0xFFFFFFFFFFFFFFFFL, getLong(bytes, 5));
		assertEquals(0x1L, getLong(bytes, 13));

		ZInputStream input = new ZInputStream(new ByteArrayInputStream(bytes));
		assertEquals(0xAB433, input.readLong());
		eof(input);
	}

	@Test
	public void should_work_with_a_zet_with_one_element_of_level_7() throws IOException {
		ZOutputStream stream = new ZOutputStream(this.stream);
		stream.writeLong(0x1122334455667733L);
		stream.close();
		byte[] bytes = bytes();
		assertEquals(26, bytes.length);
		assertEquals(7, bytes[0]);
		assertEquals(0x11, bytes[1]);
		assertEquals(0x22, bytes[2]);
		assertEquals(0x33, bytes[3]);
		assertEquals(0x44, bytes[4]);
		assertEquals(0x55, bytes[5]);
		assertEquals(0x66, bytes[6]);
		assertEquals(0x77, bytes[7]);
		assertEquals(1, bytes[8]);
		assertEquals(0x33, bytes[9]);
		assertEquals(0xFFFFFFFFFFFFFFFFL, getLong(bytes, 10));
		assertEquals(0x1L, getLong(bytes, 18));

		ZInputStream input = new ZInputStream(new ByteArrayInputStream(bytes));
		assertEquals(0x1122334455667733L, input.readLong());
		eof(input);
	}

	@Test
	public void should_work_with_a_zet_with_256_elements_of_level_6() throws IOException {
		ZOutputStream stream = new ZOutputStream(this.stream);
		long base = 0x11223344556600L;
		for (int i = 0; i < 256; i++) stream.writeLong(base + i);
		stream.close();
		byte[] bytes = bytes();
		assertEquals(280, bytes.length);
		assertEquals(6, bytes[0]);
		assertEquals(0x11, bytes[1]);
		assertEquals(0x22, bytes[2]);
		assertEquals(0x33, bytes[3]);
		assertEquals(0x44, bytes[4]);
		assertEquals(0x55, bytes[5]);
		assertEquals(0x66, bytes[6]);
		assertEquals((byte) 0x0, bytes[7]);
		for (int i = 0; i < 256; i++) assertEquals((byte) i, bytes[8 + i]);
		assertEquals(0xFFFFFFFFFFFFFFFFL, getLong(bytes, 264));
		assertEquals(256L, getLong(bytes, 272));

		ZInputStream input = new ZInputStream(new ByteArrayInputStream(bytes));
		for (int i = 0; i < 256; i++) assertEquals(base + i, input.readLong());
		eof(input);
	}

	@Test
	public void should_work_with_a_zet_with_128_elements_of_level_4() throws IOException {
		ZOutputStream stream = new ZOutputStream(this.stream);
		long base = 0x1122334400L;
		for (int i = 0; i < 128; i++) stream.writeLong(base + i);
		stream.close();
		byte[] bytes = bytes();
		assertEquals(150, bytes.length);
		assertEquals(4, bytes[0]);
		assertEquals(0x11, bytes[1]);
		assertEquals(0x22, bytes[2]);
		assertEquals(0x33, bytes[3]);
		assertEquals(0x44, bytes[4]);
		assertEquals((byte) 0x80, bytes[5]);
		for (int i = 0; i < 128; i++) assertEquals((byte) i, bytes[6 + i]);
		assertEquals(0xFFFFFFFFFFFFFFFFL, getLong(bytes, 134));
		assertEquals(128L, getLong(bytes, 142));

		ZInputStream input = new ZInputStream(new ByteArrayInputStream(bytes));
		for (int i = 0; i < 128; i++) assertEquals(base + i, input.readLong());
		eof(input);
	}

	@Test
	public void should_work_with_a_zet_2_sibling_bases_of_128_elements() throws IOException {
		ZOutputStream stream = new ZOutputStream(this.stream);
		long base1 = 0x1122334400L;
		for (int i = 0; i < 128; i++) stream.writeLong(base1 + i);
		long base2 = 0x1122445500L;
		for (int i = 0; i < 128; i++) stream.writeLong(base2 + i);
		stream.close();
		byte[] bytes = bytes();
		assertEquals(282, bytes.length);
		assertEquals(4, bytes[0]);
		assertEquals(0x11, bytes[1]);
		assertEquals(0x22, bytes[2]);
		assertEquals(0x33, bytes[3]);
		assertEquals(0x44, bytes[4]);
		assertEquals((byte) 0x80, bytes[5]);
		for (int i = 0; i < 128; i++) assertEquals((byte) i, bytes[6 + i]);
		assertEquals(2, bytes[134]);
		assertEquals(0x44, bytes[135]);
		assertEquals(0x55, bytes[136]);
		assertEquals((byte) 0x80, bytes[137]);
		for (int i = 0; i < 128; i++) assertEquals((byte) i, bytes[138 + i]);
		assertEquals(0xFFFFFFFFFFFFFFFFL, getLong(bytes, 266));
		assertEquals(256L, getLong(bytes, 274));


		ZInputStream input = new ZInputStream(new ByteArrayInputStream(bytes));
		for (int i = 0; i < 128; i++) assertEquals(base1 + i, input.readLong());
		for (int i = 0; i < 128; i++) assertEquals(base2 + i, input.readLong());
		eof(input);
	}

	@Test
	public void should_work_with_a_zet_2_bases_of_128_elements() throws IOException {
		ZOutputStream stream = new ZOutputStream(this.stream);
		long base1 = 0x1122334400L;
		for (int i = 0; i < 128; i++) stream.writeLong(base1 + i);
		long base2 = 0xAABBCCDDEE00L;
		for (int i = 0; i < 128; i++) stream.writeLong(base2 + i);
		stream.close();
		byte[] bytes = bytes();
		assertEquals(285, bytes.length);
		assertEquals(4, bytes[0]);
		assertEquals(0x11, bytes[1]);
		assertEquals(0x22, bytes[2]);
		assertEquals(0x33, bytes[3]);
		assertEquals(0x44, bytes[4]);
		assertEquals((byte) 0x80, bytes[5]);
		for (int i = 0; i < 128; i++) assertEquals((byte) i, bytes[6 + i]);
		assertEquals(5, bytes[134]);
		assertEquals((byte) 0xAA, bytes[135]);
		assertEquals((byte) 0xBB, bytes[136]);
		assertEquals((byte) 0xCC, bytes[137]);
		assertEquals((byte) 0xDD, bytes[138]);
		assertEquals((byte) 0xEE, bytes[139]);
		assertEquals((byte) 0x80, bytes[140]);
		for (int i = 0; i < 128; i++) assertEquals((byte) i, bytes[141 + i]);
		assertEquals(0xFFFFFFFFFFFFFFFFL, getLong(bytes, 269));
		assertEquals(256L, getLong(bytes, 277));


		ZInputStream input = new ZInputStream(new ByteArrayInputStream(bytes));
		for (int i = 0; i < 128; i++) assertEquals(base1 + i, input.readLong());
		for (int i = 0; i < 128; i++) assertEquals(base2 + i, input.readLong());
		eof(input);
	}

	private long getLong(byte[] bytes, int pos) {
		byte[] by = new byte[8];
		System.arraycopy(bytes, pos, by, 0, 8);
		long value = 0;
		for (int i = 0; i < by.length; i++) value = (value << 8) + ((long) by[i] & 0xFF);
		return value;
	}

	private byte[] bytes() {
		return stream.toByteArray();
	}
}
