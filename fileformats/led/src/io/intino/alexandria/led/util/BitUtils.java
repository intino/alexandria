package io.intino.alexandria.led.util;


import org.apache.commons.lang3.StringUtils;

public final class BitUtils {

	public static long read(long n, int offset, int bitCount) {
		return (n >> offset) & ones(bitCount);
	}

	public static long write(long n, long value, int offset, int bitCount) {
		return (n & zeros(bitCount, offset)) | (value << offset);
	}

	public static long bitmask(int shift) {
		return shift == Long.SIZE ? -1L : (1L << shift);
	}

	public static long ones(int shift) {
		final long bitmask = bitmask(shift);
		return bitmask == -1 ? bitmask : bitmask - 1;
	}

	public static long ones(int n, int shift) {
		return (ones(n) << shift);
	}

	public static long zeros(int n, int shift) {
		return ~ones(n, shift);
	}

	public static String toBinaryString(long value, int padding) {
		return StringUtils.leftPad(Long.toBinaryString(value), padding, '0');
	}

	public static int byteIndex(int bitIndex) {
		return bitIndex / Byte.SIZE;
	}

	public static int bitIndex(int byteIndex) {
		return byteIndex * Byte.SIZE;
	}

	public static int offsetOf(int bitIndex) {
		return bitIndex % Byte.SIZE;
	}

	public static int roundUp2(int n, int multiple) {
		return (n + multiple - 1) & (-multiple);
	}

	public static int bitsUsed(long value) {
		return Long.SIZE - Long.numberOfLeadingZeros(value);
	}

	public static double maxPossibleNumber(int numberOfBits) {
		return Math.pow(2, numberOfBits) / 2 - 1;
	}

	public static int getMinimumBytesForBits(int bitIndex, int bitCount) {
		final int bitOffset = offsetOf(bitIndex);
		final int numBytes = (int)Math.ceil((bitOffset + bitCount) / 8.0);
		return roundToJavaPrimitiveSize(numBytes);
	}

	public static int roundToJavaPrimitiveSize(int n) {
		if (n == 1 || n == 2 || n == 4 || n == 8) {
			return n;
		}
		if (n < 4) {
			return 4;
		}
		return 8;
	}

	public static String toBinaryString(long value, int padding, int splitSize) {

		final String str = toBinaryString(value, padding);

		if (splitSize == 0) {
			return str;
		}

		StringBuilder sb = new StringBuilder(str);

		int offset = splitSize;
		for (int i = 0; i < str.length() / splitSize; i++) {
			sb.insert(offset, ' ');
			offset += splitSize + 1;
		}

		return sb.toString();
	}

	private BitUtils() {
	}
}
