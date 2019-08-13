package io.intino.alexandria.bpm;

public abstract class Task {
	private final Type type;

	public Task(Type type) {
		this.type = type;
	}

	public boolean accept() {
		return true;
	}

	public abstract Result execute();

	Type type(){
		return type;
	}

	public enum Type {Automatic, Manual, CallActivity}

	public static class Result {
		private String result;

		public Result(String result) {
			this.result = result;
		}

		public String result() {
			return result;
		}
	}
}
