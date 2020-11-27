package io.intino.alexandria.office;

import io.intino.alexandria.logger.Logger;

import java.io.IOException;

public abstract class MicroSiteBuilder {

	public String template() {
		try {
			return new String(MicroSiteBuilder.class.getResourceAsStream("/MicroSiteTemplate.html").readAllBytes());
		} catch (IOException e) {
			Logger.error(e);
			return "";
		}
	}

}
