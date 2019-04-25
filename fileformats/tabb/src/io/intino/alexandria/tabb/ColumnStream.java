package io.intino.alexandria.tabb;

public interface ColumnStream {
	String ColumnExtension = ".tabbc";

	String name();

	Type type();

	Mode mode();

	boolean hasNext();

	void next();

	Long key();

	Object value();

	enum Type {
		Nominal {
			@Override
			public byte[] toByteArray(Object object) {
				return object.toString().getBytes();
			}

			@Override
			public byte[] notAvailable() {
				return NotAvailable32;
			}
		},
		Long {
			@Override
			public byte[] toByteArray(Object object) {
				byte[] result = new byte[8];
				long value = (long) object;
				for (int i = 0, shift = 56; i < 8; i++, shift -= 8) result[i] = (byte) (value >> shift);
				return result;
			}

			@Override
			public byte[] notAvailable() {
				return NotAvailable64;
			}

		},
		Integer {
			@Override
			public byte[] toByteArray(Object object) {
				byte[] result = new byte[4];
				long value = (long) object;
				for (int i = 0, shift = 24; i < 8; i++, shift -= 8) result[i] = (byte) (value >> shift);
				return result;
			}

			@Override
			public byte[] notAvailable() {
				return NotAvailable32;
			}
		},

		Double {
			@Override
			public byte[] toByteArray(Object object) {
				return Long.toByteArray(toLong(object));
			}

			private long toLong(Object object) {
				return java.lang.Double.doubleToRawLongBits((java.lang.Double) object);
			}

			@Override
			public byte[] notAvailable() {
				return NotAvailable64;
			}
		},
		Boolean {
			@Override
			public byte[] toByteArray(Object object) {
				return Integer.toByteArray(toInteger(object));
			}

			private int toInteger(Object object) {
				return (Boolean) object ? 1 : 0;
			}

			@Override
			public byte[] notAvailable() {
				return NotAvailable32;
			}
		},
		Datetime {
			@Override
			public byte[] toByteArray(Object object) {
				return Integer.toByteArray(object);
			}

			@Override
			public byte[] notAvailable() {
				return NotAvailable32;
			}
		},
		Instant {
			@Override
			public byte[] toByteArray(Object object) {
				return Integer.toByteArray(object);
			}

			@Override
			public byte[] notAvailable() {
				return NotAvailable32;
			}
		};

		private static final byte[] NotAvailable64 = Type.Long.toByteArray(0x7ff00000000007a2L);
		private static final byte[] NotAvailable32 = Type.Integer.toByteArray(0x80000000);

		public abstract byte[] toByteArray(Object object);

		public abstract byte[] notAvailable();
	}

	class Mode {
		public final String[] features;

		public Mode(String... features) {
			this.features = features;
		}

	}


}
