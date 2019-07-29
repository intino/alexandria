package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.ImageAvatarNotifier;
import io.intino.alexandria.ui.utils.AvatarUtil;

public class ImageAvatar<DN extends ImageAvatarNotifier, B extends Box> extends AbstractImageAvatar<DN, B> {
	private String text;

	public ImageAvatar(B box) {
		super(box);
	}

	public void load() {
		refresh();
	}

	public ImageAvatar<DN, B> text(String text) {
		this.text = text;
		return this;
	}

	public void update(String text) {
		this.text = text;
		refresh();
	}

	@Override
	String serializedValue() {
		return AvatarUtil.generateAvatar(text, color() != null ? color() : "black");
	}

}