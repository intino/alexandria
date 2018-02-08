package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.services.push.User;

import java.net.URL;
import java.util.List;

public class Picture extends Stamp<List<URL>> {
	private String defaultPicture;

	public String defaultPicture() {
		return this.defaultPicture;
	}

	public Picture defaultPicture(String defaultPicture) {
		this.defaultPicture = defaultPicture;
		return this;
	}

	@Override
	public List<URL> objectValue(Object object, User user) {
		return value() != null ? value().value(object, user) : null;
	}

}
