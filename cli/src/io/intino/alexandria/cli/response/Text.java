package io.intino.alexandria.cli.response;

import io.intino.alexandria.cli.Response;

public class Text extends Response {
	public final String content;

	public Text(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return content;
	}
}