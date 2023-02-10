package io.intino.alexandria.cli.response;

import io.intino.alexandria.cli.Response;
import io.intino.alexandria.cli.util.MessageHelper;

import java.util.List;

public class Question extends Response {
	public final String question;
	public final List<String> options;
	private final QuestionProvider provider;

	public Question(String question, List<String> options, QuestionProvider provider) {
		this.question = question;
		this.options = options;
		this.provider = provider;
	}

	@Override
	public String toString() {
		return MessageHelper.replaceVariables(question, provider.data());
	}
}
