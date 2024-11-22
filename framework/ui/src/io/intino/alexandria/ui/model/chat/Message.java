package io.intino.alexandria.ui.model.chat;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Message {
	private Instant ts;
	private Direction direction;
	private String content;
	private List<String> attachments = new ArrayList<>();

	public static Message with(String content, Direction direction) {
		return with(content, direction, new ArrayList<>());
	}

	public static Message with(String content, Direction direction, List<String> attachments) {
		return new Message().ts(Instant.now()).content(content).direction(direction).attachments(attachments);
	}

	public enum Direction { Incoming, Outgoing }

	public Instant ts() {
		return ts;
	}

	public Message ts(Instant ts) {
		this.ts = ts;
		return this;
	}

	public Direction direction() {
		return direction;
	}

	public Message direction(Direction direction) {
		this.direction = direction;
		return this;
	}

	public String content() {
		return content;
	}

	public Message content(String content) {
		this.content = content;
		return this;
	}

	public List<String> attachments() {
		return attachments;
	}

	public Message attachments(List<String> attachments) {
		this.attachments = attachments;
		return this;
	}
}
