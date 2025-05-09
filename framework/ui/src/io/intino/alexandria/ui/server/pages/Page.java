package io.intino.alexandria.ui.server.pages;

import io.intino.alexandria.ui.services.push.UISession;

import java.util.ArrayList;
import java.util.List;

public abstract class Page {
	public UISession session;
	public String clientId;
	public String googleApiKey;
	public String device;
	public String token;
	public List<String> webDirectories = new ArrayList<>();

	public boolean hasPermissions() {
		return true;
	}

	public String redirectUrl() {
		return session.browser().baseUrl();
	}

}