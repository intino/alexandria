package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

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

	public AvatarProperties avatarProperties(Item item, ActivitySession session) {
		return avatarPropertiesLoader != null ? avatarPropertiesLoader.load(item != null ? item.object() : null, session) : null;
	}

	public Picture avatarProperties(AvatarPropertiesLoader loader) {
		this.avatarPropertiesLoader = loader;
		return this;
	}

	@Override
	public List<URL> objectValue(Object object, ActivitySession session) {
		return value() != null ? value().value(object, session) : null;
	}

	public interface AvatarPropertiesLoader {
		AvatarProperties load(Object object, ActivitySession session);
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
