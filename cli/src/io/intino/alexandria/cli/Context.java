package io.intino.alexandria.cli;

public class Context {
	private String state;
	private String[] objects;
	private Cli.CommandInfo lastCommand;

	public Context(String state, String... objects) {
		this.state = state;
		this.objects = objects;
	}

	public String state() {
		return state;
	}

	public void state(String state) {
		this.state = state;
	}

	public String[] getObjects() {
		return objects;
	}

	public void objects(String... objects) {
		this.objects = objects;
	}

	public Cli.CommandInfo lastCommand() {
		return lastCommand;
	}

	public void lastCommand(Cli.CommandInfo info) {
		this.lastCommand = info;
	}
}
