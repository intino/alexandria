package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.OpenPageNotifier;

public class OpenPage<DN extends OpenPageNotifier, B extends Box> extends AbstractOpenPage<DN, B> {
	private String path;

    public OpenPage(B box) {
        super(box);
    }

    public OpenPage path(String path) {
    	this.path = path;
    	return this;
	}

	@Override
	public void execute() {
		if (path == null) return;
		notifier.redirect(path);
    }

}