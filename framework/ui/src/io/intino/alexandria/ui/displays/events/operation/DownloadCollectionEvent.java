package io.intino.alexandria.ui.displays.events.operation;

import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.Collection;
import io.intino.alexandria.ui.displays.events.Event;

import java.util.List;

public class DownloadCollectionEvent extends Event {
	private final List<Collection> collectionList;

	public DownloadCollectionEvent(Display sender, List<Collection> collectionList) {
		super(sender);
		this.collectionList = collectionList;
	}

	@SuppressWarnings("unchecked")
	public <C extends Collection> List<C> collections() { return (List<C>) collectionList; }
}
