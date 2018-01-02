package io.intino.konos.alexandria.activity.displays.providers;

import io.intino.konos.alexandria.activity.displays.AlexandriaCatalog;
import io.intino.konos.alexandria.activity.displays.AlexandriaStamp;

public interface AlexandriaStampProvider {
    AlexandriaStamp embeddedDisplay(String stamp);
    AlexandriaCatalog embeddedCatalog(String stamp);
}
