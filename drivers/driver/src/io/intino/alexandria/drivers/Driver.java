package io.intino.alexandria.drivers;

import java.net.URL;

public interface Driver<Info, RunResult> {
	Info info(String program);
	boolean isPublished(String program);
	URL publish(Program program);
	void update(Program program);
	void unPublish(String program);
	RunResult run(Program program);
}
