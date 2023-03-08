package io.intino.alexandria.message;

class NullValue implements Message.Value {

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public String data() {
		return null;
	}

	@Override
	public <T> T as(Class<T> type) {
		return null;
	}
}
