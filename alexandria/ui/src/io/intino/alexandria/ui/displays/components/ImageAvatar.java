package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.utils.AvatarUtil;

public class ImageAvatar<B extends Box> extends AbstractImageAvatar<B> {
	private String text;

	public ImageAvatar(B box) {
		super(box);
	}

	public String value() {
		return generateAvatar();
	}

	public ImageAvatar<B> text(String text) {
		this.text = text;
		return this;
	}

	public void update(String text) {
		this.text = text;
		refresh();
	}

	public void refresh() {
		notifier.refresh(value());
	}

	private String generateAvatar() {
		return AvatarUtil.generateAvatar(text, color());
	}

}