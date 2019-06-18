package io.intino.alexandria.drivers.shiny;

import io.intino.alexandria.drivers.Program;
import io.intino.alexandria.drivers.shiny.functions.CleanQueryParam;
import io.intino.alexandria.proxy.ProxyAdapter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class Driver implements io.intino.alexandria.drivers.Driver<URL, io.intino.alexandria.proxy.Proxy> {

	public static final String Program = "Program";
	public static final String LocalUrlParameter = "LocalUrlParameter";

	@Override
	public boolean isPublished(String program) {
		return true;
	}

	@Override
	public URL info(String program) {
		try {
//			return new URL(String.format("http://10.13.13.37:3838/%s", program));
			return new URL("http://localhost:1111");
		} catch (MalformedURLException e) {
			return null;
		}
	}

	@Override
	public URL publish(Program program) {
		try {
//			return new URL(String.format("http://10.13.13.37:3838/%s", program));
			return new URL("http://localhost:1111");
		} catch (MalformedURLException e) {
			return null;
		}
	}

	@Override
	public void unPublish(String program) {
	}

	public io.intino.alexandria.proxy.Proxy run(Map<String, Object> parameters) {
		String program = (String) parameters.get(Program);
		URL localUrl = (URL) parameters.get(LocalUrlParameter);
		return new io.intino.alexandria.proxy.Proxy(localUrl, info(program)).adapter(proxyAdapter());
	}

	private ProxyAdapter proxyAdapter() {
		return (localUrl, remoteUrl, param, value) -> {
			CleanQueryParam cleanQueryParam = new CleanQueryParam();
			return cleanQueryParam.execute(localUrl, param, value);
		};
	}
}
