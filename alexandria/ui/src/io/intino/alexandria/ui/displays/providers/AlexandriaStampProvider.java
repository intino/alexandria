package io.intino.alexandria.ui.displays.providers;

import io.intino.alexandria.ui.displays.AlexandriaAbstractCatalog;
import io.intino.alexandria.ui.displays.AlexandriaDialog;
import io.intino.alexandria.ui.displays.AlexandriaStamp;

public interface AlexandriaStampProvider {
    AlexandriaStamp embeddedDisplay(String stamp);
    AlexandriaDialog embeddedDialog(String stamp);
    AlexandriaAbstractCatalog embeddedCatalog(String stamp);
    void refreshElement();
    void refreshItem();
    void refreshItem(boolean highlight);
    void resizeItem();
}
