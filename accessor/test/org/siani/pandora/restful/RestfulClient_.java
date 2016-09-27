package org.siani.pandora.restful;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.siani.pandora.restful.core.Resource;
import org.siani.pandora.restful.core.RestfulAccessor;
import org.siani.pandora.restful.exceptions.RestfulFailure;
import org.siani.pandora.server.actions.Action;
import org.siani.pandora.server.actions.AdapterProxy;
import org.siani.pandora.server.actions.RequestAdapter;
import org.siani.pandora.server.actions.ResponseAdapter;
import org.siani.pandora.server.web.actions.DefaultResponseAdapter;
import org.siani.pandora.server.web.actions.SparkRouter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import static java.lang.Thread.sleep;
import static org.siani.pandora.server.actions.Router.Method.Post;

public class RestfulClient_ {
	private static final String BaseUrl = "http://localhost:8080";

	@BeforeClass
	public static void setUpClass() throws Exception {
		SparkRouter router = new SparkRouter(8080, new AdapterProxy() {

			@Override
			public RequestAdapter requestAdapterOf(String name, Class clazz) {
				return null;//TODO
			}

			@Override
			public ResponseAdapter responseAdapterOf(String s) {
				return new DefaultResponseAdapter();
			}
		});
		router.route("/encoding").as(Post).with(new EncodingAction());
		sleep(1000);
	}

	@Test
	public void should_post_data_encoded_with_utf8() throws IOException, RestfulFailure {
		RestfulApi client = new RestfulAccessor();
		RestfulApi.Response post = client.post(localhost(), "/encoding", new HashMap<String, String>() {{
			put("param1", "holá");
			put("param2", "adiós");
		}});
		Assert.assertEquals("name: param1, value: holá;name: param2, value: adiós;", post.content());
	}

	@Test
	public void should_post_resource_encoded_with_utf8() throws IOException, RestfulFailure {
		RestfulApi client = new RestfulAccessor();
		Resource resource = new Resource(new ByteArrayInputStream(new byte[0]), "application/octet-stream");
		resource.addParameter("param1", "holá");
		resource.addParameter("param2", "adiós");
		RestfulApi.Response post = client.post(localhost(), "/encoding", resource);
		Assert.assertEquals("name: param1, value: holá;name: param2, value: adiós;filesCount: 1", post.content());
	}

	private URL localhost() {
		try {
			return new URL(BaseUrl);
		} catch (MalformedURLException e) {
			return null;
		}
	}

	public static class EncodingAction implements Action {

		public EncodingAction() {
		}

		public Task<Input, Output> task() {
			return (input, output) -> {
				StringBuilder result = new StringBuilder();
				input.parameters().entrySet().forEach(entry -> result.append(String.format("name: %s, value: %s;", entry.getKey(), entry.getValue().toString())));

				if (input.files().size() > 0)
					result.append("filesCount: " + input.files().size());

				output.write(result.toString());
			};
		}
	}

}