package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.FileNotifier;

import java.net.URL;

public class File<DN extends FileNotifier, B extends Box> extends AbstractFile<DN, B> {

	public File(B box) {
		super(box);
	}

	@Override
	public void init() {
		super.init();
		refresh();
	}

	public void value(URL value) {
		_value(value);
		refresh();
	}

	public void refresh() {
		notifier.refresh(info());
	}


}