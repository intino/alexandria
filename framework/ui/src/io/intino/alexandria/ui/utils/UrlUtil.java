package io.intino.alexandria.ui.utils;

import io.intino.alexandria.logger.Logger;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlUtil {

	public static URL toURL(String url) {
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			Logger.error(e);
			return null;
		}
	}

}
