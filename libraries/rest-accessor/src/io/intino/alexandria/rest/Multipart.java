package io.intino.alexandria.rest;

import io.intino.alexandria.Resource;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.restaccessor.adapters.RequestAdapter;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Multipart {
	private final MultipartEntityBuilder builder = MultipartEntityBuilder.create();

	public void addPart(Resource resource) {
		builder.addBinaryBody(resource.name(), resource.stream(), ContentType.create(resource.metadata().contentType()), resource.name());
	}

	public void addPart(Object object, String name) {
		builder.addPart(name, new StringBody(RequestAdapter.adapt(object), ContentType.APPLICATION_JSON));
	}

	public InputStream content() {
		final HttpEntity build = builder.build();
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			build.writeTo(outputStream);
			return new ByteArrayInputStream(outputStream.toByteArray());
		} catch (IOException e) {
			Logger.error(e);
			return InputStream.nullInputStream();
		}
	}
}
