package io.intino.konos.alexandria.ui.displays.providers;

import io.intino.konos.alexandria.ui.displays.AlexandriaAbstractCatalog;
import io.intino.konos.alexandria.ui.displays.AlexandriaDialog;
import io.intino.konos.alexandria.ui.displays.AlexandriaStamp;

public interface AlexandriaStampProvider {
    AlexandriaStamp embeddedDisplay(String stamp);
    AlexandriaDialog embeddedDialog(String stamp);
    AlexandriaAbstractCatalog embeddedCatalog(String stamp);
    void refreshElement();
    void refreshItem();
}
