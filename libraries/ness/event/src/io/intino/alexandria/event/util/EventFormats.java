package io.intino.alexandria.event.util;

import io.intino.alexandria.event.Event;

import java.io.File;

public class EventFormats {

	public static Event.Format formatOf(File file) {
		String name = file.getName();
		int index = name.lastIndexOf('.');
		return Event.Format.byExtension(index < 0 ? "" : name.substring(index));
	}
}
