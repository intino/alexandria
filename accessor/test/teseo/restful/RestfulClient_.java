package teseo.restful;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import teseo.framework.actions.Action;
import teseo.framework.actions.AdapterProxy;
import teseo.framework.actions.RequestAdapter;
import teseo.framework.actions.ResponseAdapter;
import teseo.framework.web.actions.DefaultResponseAdapter;
import teseo.framework.web.actions.SparkRouter;
import teseo.restful.core.Resource;
import teseo.restful.core.RestfulAccessor;
import teseo.restful.exceptions.RestfulFailure;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import static java.lang.Thread.sleep;
import static teseo.framework.actions.Router.Method.Post;

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