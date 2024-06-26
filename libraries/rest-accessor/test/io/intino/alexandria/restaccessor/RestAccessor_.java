package io.intino.alexandria.restaccessor;

import io.intino.alexandria.Resource;
import io.intino.alexandria.restaccessor.core.RestAccessor;
import io.intino.alexandria.restaccessor.exceptions.RestfulFailure;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class RestAccessor_ {
	private static final String BaseUrl = "http://localhost:8080";

	@Test
	public void should_post_data_encoded_with_utf8() throws RestfulFailure {
		io.intino.alexandria.restaccessor.RestAccessor client = new RestAccessor();
		Response post = client.post(localhost(), "/encoding", new HashMap<>() {{
			put("param1", "holá");
			put("param2", "adiós");
		}});

		Assert.assertEquals("name: param1, value: holá;name: param2, value: adiós;", post.content());
	}

	@Test
	public void should_post_resource_encoded_with_utf8() throws RestfulFailure {
		io.intino.alexandria.restaccessor.RestAccessor client = new RestAccessor();
		Resource resource = new Resource("example", new ByteArrayInputStream(new byte[0]));
//		resource.addParameter("param1", "holá");
//		resource.addParameter("param2", "adiós");
		Response post = client.post(localhost(), "/encoding", resource);
		Assert.assertEquals("name: param1, value: holá;name: param2, value: adiós;filesCount: 1", post.content());
	}

	private URL localhost() {
		try {
			return new URL(BaseUrl);
		} catch (MalformedURLException e) {
			return null;
		}
	}

	@BeforeClass
	public static void setUpClass() {
	}

}