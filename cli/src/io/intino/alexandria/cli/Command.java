package io.intino.alexandria.cli;

import io.intino.alexandria.cli.command.MessageProperties;

public interface Command {
	Response execute(MessageProperties properties, String command, String... args);
}
