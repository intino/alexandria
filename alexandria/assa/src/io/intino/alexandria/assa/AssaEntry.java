package io.intino.alexandria.assa;

public class AssaEntry {
	long key;
	int value;

	AssaEntry(long key, int value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public int hashCode() {
		return Long.hashCode(key);
	}
}
