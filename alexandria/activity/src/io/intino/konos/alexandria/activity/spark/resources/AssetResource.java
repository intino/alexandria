package io.intino.konos.alexandria.activity.spark.resources;

import io.intino.konos.alexandria.activity.displays.AlexandriaDisplayNotifierProvider;
import io.intino.konos.alexandria.activity.spark.ActivitySparkManager;
import io.intino.konos.alexandria.activity.spark.AssetLoader;
import io.intino.konos.alexandria.activity.spark.resources.exceptions.AssetNotFoundException;
import io.intino.konos.alexandria.activity.utils.StreamUtil;
import io.intino.konos.alexandria.exceptions.AlexandriaException;
import spark.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
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
        InputStream inputStream = null;

        if (isAssetPathRelative(name))
            name = loader.asset(name).toString();

        try {
            inputStream = new URL(name).openStream();
            if (contentType != null && !contentType.isEmpty()) {
                byte[] content = IOUtils.toByteArray(inputStream);
                manager.write(new String(content, "UTF-8"), contentType);
            }
            else
                manager.write(inputStream, manager.fromQuery("label", String.class), manager.fromQuery("embedded", Boolean.class));
        } catch (IOException e) {
            manager.write(new AssetNotFoundException());
        }
        finally {
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
        return new String(Base64.getDecoder().decode(manager.fromPath("name", String.class)));
    }

    private String assetContentTypeOf() {
        String contentType = manager.fromQuery("contentType", String.class);

        if (contentType == null)
            return null;

        return new String(Base64.getDecoder().decode(contentType));
    }

}
