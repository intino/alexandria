package io.intino.alexandria.cli.response;

public class QuestionProvider {
	private MessageData data = new MessageData();

	public QuestionProvider add(String variable, String value) {
		data.add(variable, value);
		return this;
	}

	public MessageData data() {
		return data;
	}
}
