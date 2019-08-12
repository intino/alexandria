package io.intino.alexandria.bpm;

public class Callback {
	private String value;
	private String requesterId;
	private String requesterState;

	public Callback(String value, String requesterId, String requesterState) {
		this.value = value;
		this.requesterId = requesterId;
		this.requesterState = requesterState;
	}

	public String value() {
		return value;
	}

	public String requesterId() {
		return requesterId;
	}

	public String requesterState() {
		return requesterState;
	}

	public static Callback from(String data) {
		String[] split = data.split(";");
		return new Callback(split[0], split[1], split[2]);
	}

	@Override
	public String toString() {
		return value + ";" + requesterId + ";" + requesterState;
	}
}
