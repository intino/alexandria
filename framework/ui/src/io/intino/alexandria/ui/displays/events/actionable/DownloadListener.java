package io.intino.alexandria.ui.displays.events.actionable;

import io.intino.alexandria.ui.spark.UIFile;

public interface DownloadListener {
	UIFile accept(DownloadEvent event);
}
