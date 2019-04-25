package io.intino.alexandria.ui.model.mold.stamps;

import io.intino.alexandria.ui.model.mold.Stamp;
import io.intino.alexandria.ui.model.Item;
import io.intino.alexandria.ui.services.push.UISession;

import java.net.URL;
import java.util.List;

public class Picture extends Stamp<List<URL>> {
	private String defaultPicture;
	private AvatarPropertiesLoader avatarPropertiesLoader;

	public String defaultPicture() {
		return this.defaultPicture;
	}

	public Picture defaultPicture(String defaultPicture) {
		this.defaultPicture = defaultPicture;
		return this;
	}

	public boolean isAvatar() {
		return avatarPropertiesLoader != null;
	}

	public AvatarProperties avatarProperties(Item item, UISession session) {
		return avatarPropertiesLoader != null ? avatarPropertiesLoader.load(item != null ? item.object() : null, session) : null;
	}

	public Picture avatarProperties(AvatarPropertiesLoader loader) {
		this.avatarPropertiesLoader = loader;
		return this;
	}

	@Override
	public List<URL> objectValue(Object object, UISession session) {
		return value() != null ? value().value(object, session) : null;
	}

	public interface AvatarPropertiesLoader {
		AvatarProperties load(Object object, UISession session);
	}

	public static class AvatarProperties {
		private String text;
		private String color;

		public String text() {
			return text;
		}

		public AvatarProperties text(String text) {
			this.text = text;
			return this;
		}

		public String color() {
			return color;
		}

		public AvatarProperties color(String color) {
			this.color = color;
			return this;
		}
	}
}
