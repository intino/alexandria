package io.intino.konos.server.activity.displays.layouts.builders;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import io.intino.konos.server.activity.displays.layouts.model.Layout;
import io.intino.konos.server.activity.displays.schemas.PlatformInfo;

import java.io.IOException;
import java.net.URL;

import static org.apache.tika.io.IOUtils.toByteArray;

public class PlatformInfoBuilder {

    public static PlatformInfo build(Layout.Settings settings) {
        PlatformInfo info = new PlatformInfo().title(settings.title())
                .subtitle(settings.subtitle())
                .logo(logo(settings));

        if (settings.authServiceUrl() != null)
            info.authServiceUrl(settings.authServiceUrl().toString());

        return info;
    }

    private static String logo(Layout.Settings settings) {
        try {
            return toBase64(settings.logo());
        } catch (Exception e) {
            return "";
        }
    }

    private static String toBase64(URL resource) throws IOException {
        return "data:image/png;base64," + Base64.encode(toByteArray(resource.openStream()));
    }

}