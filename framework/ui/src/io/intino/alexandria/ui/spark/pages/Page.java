package io.intino.alexandria.ui.spark.pages;

import io.intino.alexandria.ui.Soul;
import io.intino.alexandria.ui.services.push.UIClient;
import io.intino.alexandria.ui.services.push.UISession;

public abstract class Page {
	public UISession session;
	public String clientId;
	public String googleApiKey;
	public String device;
	public String token;

	public boolean hasPermissions() {
		return true;
	}

	public String redirectUrl() {
		return session.browser().baseUrl();
	}

}