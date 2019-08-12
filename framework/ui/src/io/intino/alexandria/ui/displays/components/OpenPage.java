package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.OpenPageNotifier;

public class OpenPage<DN extends OpenPageNotifier, B extends Box> extends AbstractOpenPage<DN, B> {
	private String path;

    public OpenPage(B box) {
        super(box);
    }

	public void execute() {
		if (path == null) return;
		notifier.open(path);
    }

    public OpenPage path(String path) {
    	_path(path);
    	return this;
	}

	protected OpenPage _path(String path) {
		this.path = path;
		return this;
	}

}