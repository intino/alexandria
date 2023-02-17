package io.intino.alexandria.datalake.file.message;

import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.event.message.MessageEvent;

import java.io.File;

public class MessageEventTank implements Datalake.Store.Tank<MessageEvent> {
	private final File root;

	MessageEventTank(File root) {
		this.root = root;
	}

	@Override
	public String name() {
		return root.getName();
	}

	public Datalake.Store.Source<MessageEvent> source(String name) {
		return new MessageEventSource(new File(root, name));
	}

	public File root() {
		return root;
	}


}