package io.intino.alexandria.drivers.shiny.functions;

import java.net.URL;

public class CleanQueryParam {
	public String execute(URL localUrl, String key, String value) {
		String path = localUrl.getPath();
		String token = path.substring(path.lastIndexOf("/")+1);
		return value.replace(token, "");
	}
}
