package io.intino.konos.alexandria.activity.displays.providers;

import io.intino.konos.alexandria.activity.displays.AlexandriaAbstractCatalog;
import io.intino.konos.alexandria.activity.displays.AlexandriaDialog;
import io.intino.konos.alexandria.activity.displays.AlexandriaStamp;

public interface AlexandriaStampProvider {
    AlexandriaStamp embeddedDisplay(String stamp);
    AlexandriaDialog embeddedDialog(String stamp);
    AlexandriaAbstractCatalog embeddedCatalog(String stamp);
    void fullRefresh();
}
