package io.intino.alexandria.tabb;

import static java.nio.ByteBuffer.allocate;

public interface ColumnStream {
	String ColumnExtension = ".tabbc";

	String name();

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
                return new byte[] {0};
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
		};

		private static final byte[] NotAvailable64 = Type.Long.toByteArray(0x7ff00000000007a2L);
		private static final byte[] NotAvailable32 = Type.Integer.toByteArray(0x80000000);

		public abstract byte[] toByteArray(Object object);

		public abstract byte[] notAvailable();
	}


}
