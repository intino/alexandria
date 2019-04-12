package io.intino.alexandria.tabb;

import java.text.SimpleDateFormat;
import java.util.Date;

import static java.nio.ByteBuffer.allocate;
import static java.nio.ByteBuffer.wrap;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

public interface ColumnStream {
	String ColumnExtension = ".tabbc";

	String name();

	Type type();

	Mode mode();

	boolean hasNext();

	void next();

	Long key();

	byte[] value();

	enum Type {
		Nominal {
			public String toString(byte[] bytes) {
				return new String(bytes);
			}
		},
		Long {
			public String toString(byte[] bytes) {
				return wrap(bytes).getLong() + "";
			}
		},
		Integer {
			public String toString(byte[] bytes) {
				return wrap(bytes).getInt() + "";
			}
		},
		Double {
			public String toString(byte[] bytes) {
				return wrap(bytes).getDouble() + "";
			}
		},
		Boolean {
			public String toString(byte[] bytes) {
				return (wrap(bytes).getInt() == 1) + "";
			}
		},
		Datetime {
			public String toString(byte[] bytes) {
				Date date = new Date(MILLISECONDS.convert(wrap(bytes).getInt(), SECONDS));
				return new SimpleDateFormat().format(date);
			}
		},
		Instant {
			public String toString(byte[] bytes) {
				Date date = Date.from(java.time.Instant.ofEpochMilli(wrap(bytes).getLong()));
				return new SimpleDateFormat().format(date);
			}
		};


		public String toString(byte[] bytes) {
			return "";
		}
	}

	class Mode {
		public final String[] features;

		public Mode(String... features) {
			this.features = features;
		}

	}

	class NotAvailable {
		static final long NaDouble = 0x7ff00000000007a2L;
		static final int NaInt = 0x80000000;
		static final Long NaLong = null;
		private static final byte[] NaDoubleBytes = allocate(8).putDouble(NaDouble).array();
		private static final byte[] NaIntBytes = allocate(4).putInt(NaInt).array();

		public static byte[] bytesOf(Type type) {
			return type.equals(Type.Double) ? NaDoubleBytes : NaIntBytes;
		}
	}

}
