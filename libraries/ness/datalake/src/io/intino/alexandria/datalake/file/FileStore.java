package io.intino.alexandria.datalake.file;

import java.io.File;

public interface FileStore {
	String fileExtension();

	File directory();
}
