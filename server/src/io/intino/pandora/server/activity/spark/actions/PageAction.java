package io.intino.pandora.server.activity.spark.actions;

import cottons.utils.StreamHelper;
import io.intino.pandora.server.activity.services.push.Browser;
import org.bouncycastle.util.encoders.Base64;

import java.io.IOException;
import java.net.URL;

import static java.lang.String.format;
import static spark.utils.IOUtils.toByteArray;


public abstract class PageAction {
	private final String activityName;

	public io.intino.pandora.server.activity.services.push.ActivitySession session;
	public String clientId;

	private static final String TemplateName = "/www/%s/%s.html";

	public PageAction(String activityName) {
		this.activityName = activityName;
	}

	protected abstract String title();
	protected abstract String subtitle();
	protected abstract URL logo();

	protected String template(String name) {
		String sessionId = session.id();
		String language = session.discoverLanguage();
		Browser browser = session.browser();

		try {
			byte[] templateBytes = StreamHelper.readBytes(PageAction.class.getResourceAsStream(format(TemplateName, activityName, name)));
			String result = new String(templateBytes);

			result = result.replace("$title", title());
			result = result.replace("$subtitle", subtitle());
			result = result.replace("$language", language);
			result = result.replace("$currentSession", sessionId);
			result = result.replace("$client", clientId);
			result = result.replace("$baseUrl", browser.baseUrl().toString());
			result = result.replace("$url", browser.baseUrl().toString() + "/" + activityName);
			result = result.replace("$pushUrl", browser.pushUrl(sessionId, clientId, language).toString());

			if (logo() != null)
				result = result.replace("$logo", encode(logo()));

			return result;
		} catch (IOException e) {
			return "";
		}
	}

	private String encode(URL logo) {
		try {
			return "data:image/png;base64," + Base64.encode(toByteArray(logo.openStream()));
		} catch (IOException e) {
			return "";
		}
	}

}