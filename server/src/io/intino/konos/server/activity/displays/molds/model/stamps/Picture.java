package io.intino.konos.server.activity.displays.molds.model.stamps;

import io.intino.konos.server.activity.displays.molds.model.Stamp;

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
	public List<URL> objectValue(Object object, String username) {
		return value() != null ? value().value(object, username) : null;
	}

}
