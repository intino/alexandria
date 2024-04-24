package io.intino.alexandria.cli.command;

import io.intino.alexandria.cli.Context;
import io.intino.alexandria.cli.response.Attachment;

public interface MessageProperties {
	String channel();
	String token();
	String ts();
	String timeZone();
	Context context();
	String rawMessage();
	Attachment attachment();
}
