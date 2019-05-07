package io.intino.alexandria.ui.displays.events.operation;

import io.intino.alexandria.Resource;

public interface DownloadListener {
	Resource accept(DownloadEvent event);
}
