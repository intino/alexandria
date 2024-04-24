package io.intino.alexandria.cli.schemas;

public class BotTalk implements java.io.Serializable {

	private String conversation;
	private String timeZone;

	public String conversation() {
		return this.conversation;
	}

	public String timeZone() {
		return this.timeZone;
	}

	public BotTalk conversation(String conversation) {
		this.conversation = conversation;
		return this;
	}

	public BotTalk timeZone(String timeZone) {
		this.timeZone = timeZone;
		return this;
	}
}