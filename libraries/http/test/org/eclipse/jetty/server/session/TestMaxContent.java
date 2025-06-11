package org.eclipse.jetty.server.session;

import io.intino.alexandria.Resource;
import io.intino.alexandria.http.AlexandriaHttpServer;
import io.intino.alexandria.restaccessor.RestAccessor;
import io.intino.alexandria.restaccessor.exceptions.RestfulFailure;
import jakarta.servlet.http.Part;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

public class TestMaxContent {

	public static void main(String[] args) throws RestfulFailure {
		AlexandriaHttpServer<?> server = new AlexandriaHttpServer<>(9010, "/www", 3780);
		server.route("/aaa").post(e -> {
			Collection<Part> parts = e.request().raw().getParts();
			Part part = parts.stream().findFirst().orElse(null);
			System.out.println((part != null ? part.getSize() : 0) + " bytes received");
		});
		server.start();

		RestAccessor accessor = new io.intino.alexandria.restaccessor.core.RestAccessor();
		accessor.post(urlOf("http://localhost:9010"), "/aaa", new Resource("a", new ByteArrayInputStream(new byte[3781])));
	}

	private static URL urlOf(String url) {
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

}
