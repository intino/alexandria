package io.intino.alexandria.rest;

import io.intino.alexandria.Resource;
import io.intino.alexandria.logger.Logger;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;

import java.io.IOException;
import java.io.InputStream;

public class Multipart {
	private final MultipartEntityBuilder builder = MultipartEntityBuilder.create();

	public void addPart(Resource resource) {
		builder.addBinaryBody(resource.name(), resource.stream(), ContentType.create(resource.metadata().contentType()), resource.name() + "." + resource.type());
	}

	public void addPart(Object object, String name) {
		builder.addPart(name, new StringBody(ResponseAdapter.adapt(object), ContentType.APPLICATION_JSON));
	}

	public InputStream content() {
		try {
			return builder.build().getContent();
		} catch (IOException e) {
			Logger.error(e);
			return InputStream.nullInputStream();
		}
	}
}
