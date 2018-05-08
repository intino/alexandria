package io.intino.konos.alexandria.ui.spark.actions;

import java.io.InputStream;

public interface AlexandriaEditorSaveParameters {
	InputStream document();
	boolean completed();
}
