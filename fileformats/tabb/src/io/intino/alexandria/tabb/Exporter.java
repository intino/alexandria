package io.intino.alexandria.tabb;

import java.io.File;

public interface Exporter extends Generator {

	Exporter destination(File file, String name);

}
