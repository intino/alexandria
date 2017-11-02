package io.intino.alexandria.framework.box.model.mold.stamps;

import io.intino.alexandria.framework.box.model.mold.Stamp;

import java.net.URL;
import java.util.List;

public class Picture extends Stamp<List<URL>> {
	private URL defaultPicture;

	public URL defaultPicture() {
		return this.defaultPicture;
	}

	public Picture defaultPicture(URL defaultPicture) {
		this.defaultPicture = defaultPicture;
		return this;
	}

	@Override
	public List<URL> value(Object object, String username) {
		return value() != null ? value().value(object, username) : null;
	}

}
