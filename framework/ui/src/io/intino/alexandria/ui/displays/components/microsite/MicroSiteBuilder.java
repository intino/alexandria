package io.intino.alexandria.ui.displays.components.microsite;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.ui.utils.IOUtils;

import java.io.IOException;

public abstract class MicroSiteBuilder {

	public String template() {
		try {
			return new String(IOUtils.toByteArray(MicroSiteBuilder.class.getResourceAsStream("/MicroSiteTemplate.html")));
		} catch (IOException e) {
			Logger.error(e);
			return "";
		}
	}

}
