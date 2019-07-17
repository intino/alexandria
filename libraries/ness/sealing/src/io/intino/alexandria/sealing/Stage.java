package io.intino.alexandria.sealing;

import io.intino.alexandria.Session;

import java.util.stream.Stream;

public interface Stage {
	Stream<Session> sessions();

	void push(Stream<Session> sessions);

	void clear();
}