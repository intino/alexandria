package io.intino.alexandria.tabb;

import static java.nio.ByteBuffer.allocate;

public interface ColumnStream {
	String ColumnExtension = ".tabbc";

	String name();

	boolean isIndex();

	Type type();

	boolean hasNext();

	void next();

	Long key();

	Object value();

	enum Type {
		String {
			@Override
			public byte[] toByteArray(Object object) {
				if (object == null) return notAvailable();
				return (object.toString() + '\0').getBytes();
			}

			@Override
			public byte[] notAvailable() {
				return new byte[]{0};
			}


			@Override
			public Object parse(String value) {
				return value;
			}
		},
		Nominal {
			@Override
			public byte[] toByteArray(Object object) {
				if (object == null) return notAvailable();
				return Integer.toByteArray(object);
			}

			@Override
			public byte[] notAvailable() {
				return NotAvailable32;
			}

			@Override
			public Object parse(String value) {
				return value;
			}
		},
		Long {
			@Override
			public byte[] toByteArray(Object object) {
				if (object == null) return notAvailable();
				return allocate(8).putLong((java.lang.Long) object).array();
			}

			@Override
			public byte[] notAvailable() {
				return NotAvailable64;
			}


			@Override
			public Object parse(String value) {
				return java.lang.Long.valueOf(value);
			}

		},
		Integer {
			@Override
			public byte[] toByteArray(Object object) {
				if (object == null) return notAvailable();
				return allocate(4).putInt((java.lang.Integer) object).array();
			}

			@Override
			public byte[] notAvailable() {
				return NotAvailable32;
			}

			@Override
			public Object parse(String value) {
				return java.lang.Integer.valueOf(value);
			}
		},

		Double {
			@Override
			public byte[] toByteArray(Object object) {
				if (object == null) return notAvailable();
				return Long.toByteArray(toLong(object));
			}

			private long toLong(Object object) {
				return java.lang.Double.doubleToRawLongBits((java.lang.Double) object);
			}

			@Override
			public byte[] notAvailable() {
				return NotAvailable64;
			}

			@Override
			public Object parse(String value) {
				return java.lang.Double.valueOf(value);
			}
		},
		Boolean {
			@Override
			public byte[] toByteArray(Object object) {
				if (object == null) return notAvailable();
				return Integer.toByteArray(toInteger(object));
			}

			private int toInteger(Object object) {
				return (Boolean) object ? 1 : 0;
			}

			@Override
			public byte[] notAvailable() {
				return NotAvailable32;
			}

			@Override
			public Object parse(String value) {
				return java.lang.Boolean.valueOf(value);
			}
		},
		Datetime {
			@Override
			public byte[] toByteArray(Object object) {
				if (object == null) return notAvailable();
				return Integer.toByteArray(object);
			}

			@Override
			public byte[] notAvailable() {
				return NotAvailable32;
			}

			public Object parse(String value) {
				return java.time.LocalDateTime.parse(value);
			}
		},
		Instant {
			@Override
			public byte[] toByteArray(Object object) {
				if (object == null) return notAvailable();
				return Integer.toByteArray((int) (((java.time.Instant) object).toEpochMilli() / 1000));
			}

			@Override
			public byte[] notAvailable() {
				return NotAvailable32;
			}

			@Override
			public Object parse(String value) {
				return java.time.Instant.parse(value);
			}
		};

		private static final byte[] NotAvailable64 = Type.Long.toByteArray(0x7ff00000000007a2L);
		private static final byte[] NotAvailable32 = Type.Integer.toByteArray(0x80000000);

		public abstract byte[] toByteArray(Object object);

		public abstract byte[] notAvailable();

		public abstract Object parse(String value);
	}


}
