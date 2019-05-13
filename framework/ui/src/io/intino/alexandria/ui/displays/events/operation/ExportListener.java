package io.intino.alexandria.ui.displays.events.operation;

import io.intino.alexandria.ui.spark.UIFile;

public interface ExportListener {
	UIFile accept(ExportEvent event);
}
