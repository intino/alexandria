package io.intino.alexandria.proxy;

import java.net.URL;

public interface ProxyAdapter {
	String adaptParameter(URL localUrl, URL remoteUrl, String param, String value);
}
