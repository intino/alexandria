package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;

public class OpenPage<B extends Box> extends AbstractOpenPage<B> {
	private String title;
	private String path;
	private String icon;

    public OpenPage(B box) {
        super(box);
    }

	public OpenPage title(String title) {
		this.title = title;
		return this;
	}

    public OpenPage path(String path) {
    	this.path = path;
    	return this;
	}

    public OpenPage icon(String icon) {
    	this.icon = icon;
    	return this;
	}

	public void open() {
		if (path == null) return;
		notifier.redirect(path);
    }

    public void refresh() {
    	notifier.refresh(title);
	}

}