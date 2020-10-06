package io.intino.alexandria.led;

public enum FieldMask {
	Boolean(1), Byte(8), Short(16), Char(16), Int(32), Float(32), Long(64), Double(64);
	int size;

	FieldMask(int size) {
		this.size = size;
	}
}