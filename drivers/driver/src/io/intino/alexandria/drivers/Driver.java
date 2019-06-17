package io.intino.alexandria.drivers;

import java.net.URL;
import java.util.Map;

public interface Driver<Info, RunResult> {
	boolean isPublished(String program);
	Info info(String program);
	URL publish(Program program);
	void unPublish(String program);
	RunResult run(Map<String, Object> parameters);
}
