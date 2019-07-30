package io.intino.alexandria.drivers.r;

import java.io.InputStream;

public interface Result {
	String getVariable(String name);
	InputStream getFile(String filename);
	void close();
}
