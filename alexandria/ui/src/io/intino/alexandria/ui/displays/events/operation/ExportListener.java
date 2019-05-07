package io.intino.alexandria.ui.displays.events.operation;

import io.intino.alexandria.Resource;

public interface ExportListener {
	Resource accept(ExportEvent event);
}
