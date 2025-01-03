package io.intino.alexandria.ui.server.pages;

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