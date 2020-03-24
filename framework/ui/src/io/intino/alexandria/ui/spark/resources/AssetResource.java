package io.intino.alexandria.ui.spark.resources;

import io.intino.alexandria.exceptions.AlexandriaException;
import io.intino.alexandria.ui.displays.notifiers.DisplayNotifierProvider;
import io.intino.alexandria.ui.spark.AssetLoader;
import io.intino.alexandria.ui.spark.UISparkManager;
import io.intino.alexandria.ui.spark.resources.exceptions.AssetNotFoundException;
import io.intino.alexandria.ui.utils.StreamUtil;
import spark.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AssetResource extends Resource {
	private final AssetLoader loader;

	public AssetResource(AssetLoader loader, UISparkManager manager, DisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
		this.loader = loader;
	}

	@Override
	public void execute() throws AlexandriaException {
		super.execute();

		String name = assetName();
		String contentType = assetContentTypeOf();
		InputStream inputStream = null;

		if (isAssetPathRelative(name))
			name = loader.asset(name).toString();

		try {
			inputStream = new URL(name).openStream();
			if (contentType != null && !contentType.isEmpty()) {
				byte[] content = IOUtils.toByteArray(inputStream);
				manager.write(new String(content, StandardCharsets.UTF_8), contentType);
			} else {
				String label = manager.fromQueryOrDefault("label", "");
				boolean embedded = Boolean.parseBoolean(manager.fromQueryOrDefault("embedded", "false"));
				manager.write(inputStream, label, embedded);
			}
		} catch (IOException e) {
			manager.write(new AssetNotFoundException());
		} finally {
			StreamUtil.close(inputStream);
		}
	}

	private boolean isAssetPathRelative(String name) {
		try {
			new URL(name);
			return false;
		} catch (MalformedURLException e) {
			return true;
		}
	}

	private String assetName() {
		return new String(Base64.getDecoder().decode(manager.fromPath("name")));
	}

	private String assetContentTypeOf() {
		String contentType = manager.fromQuery("contentType");
		if (contentType == null) return null;
		return new String(Base64.getDecoder().decode(contentType));
	}
}
