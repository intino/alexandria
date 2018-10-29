package io.intino.alexandria.restful;

import io.intino.alexandria.restful.RestfulApi;
import io.intino.konos.alexandria.schema.Resource;
import io.intino.alexandria.restful.core.RestfulAccessor;
import io.intino.alexandria.restful.exceptions.RestfulFailure;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class RestfulClient_ {
	private static final String BaseUrl = "http://localhost:8080";

	@BeforeClass
	public static void setUpClass() throws Exception {
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
		Resource resource = new Resource("example").data(new ByteArrayInputStream(new byte[0])).contentType("application/octet-stream");
//		resource.addParameter("param1", "holá");
//		resource.addParameter("param2", "adiós");
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

}