package io.intino.konos.alexandria.foundation.activity.spark.resources;

import io.intino.konos.alexandria.exceptions.AlexandriaException;
import io.intino.konos.alexandria.foundation.activity.displays.AlexandriaDisplayNotifierProvider;
import io.intino.konos.alexandria.foundation.activity.spark.ActivitySparkManager;
import io.intino.konos.alexandria.foundation.activity.spark.AssetLoader;
import io.intino.konos.alexandria.foundation.activity.spark.resources.exceptions.AssetNotFoundException;
import spark.utils.IOUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

public class AssetResource extends Resource {
    private final AssetLoader loader;

    public AssetResource(AssetLoader loader, ActivitySparkManager manager, AlexandriaDisplayNotifierProvider notifierProvider) {
        super(manager, notifierProvider);
        this.loader = loader;
    }

    @Override
    public void execute() throws AlexandriaException {
        super.execute();

        String name = assetName();
        String contentType = assetContentTypeOf();

        if (isAssetPathRelative(name))
            name = loader.asset(name).toString();

        try {
            if (contentType != null && !contentType.isEmpty()) {
                byte[] content = IOUtils.toByteArray(new URL(name).openStream());
                manager.write(new String(content, "UTF-8"), contentType);
            }
            else
                manager.write(new URL(name).openStream(), manager.fromQuery("label", String.class));
        } catch (MalformedURLException e) {
            manager.write(new AssetNotFoundException());
        } catch (IOException e) {
            manager.write(new AssetNotFoundException());
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
        return new String(Base64.getDecoder().decode(manager.fromPath("name", String.class)));
    }

    private String assetContentTypeOf() {
        String contentType = manager.fromQuery("contentType", String.class);

        if (contentType == null)
            return null;

        return new String(Base64.getDecoder().decode(contentType));
    }

}
