package io.intino.alexandria.ui.displays.builders;

import io.intino.alexandria.ui.model.Settings;
import io.intino.alexandria.ui.schemas.PlatformInfo;

public class PlatformInfoBuilder {

    public static PlatformInfo build(Settings settings) {
        PlatformInfo info = new PlatformInfo().title(settings.title())
                .subtitle(settings.subtitle())
                .logo(settings.logo())
                .favicon(settings.favicon());

        if (settings.authServiceUrl() != null)
            info.authServiceUrl(settings.authServiceUrl().toString());

        return info;
    }

}