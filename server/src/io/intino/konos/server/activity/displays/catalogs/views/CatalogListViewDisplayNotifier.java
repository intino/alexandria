package io.intino.konos.server.activity.displays.catalogs.views;

import io.intino.konos.server.activity.displays.schemas.ElementView;
import io.intino.konos.server.activity.displays.schemas.PictureData;
import io.intino.konos.server.activity.displays.schemas.Sorting;

public class CatalogListViewDisplayNotifier extends io.intino.konos.server.activity.displays.DisplayNotifier {

    public CatalogListViewDisplayNotifier(io.intino.konos.server.activity.displays.Display display, io.intino.konos.server.activity.displays.MessageCarrier carrier) {
        super(display, carrier);
    }

	public void refreshView(ElementView value) {
		putToDisplay("refreshView", "value", value);
	}

	public void clear() {
		putToDisplay("clear");
	}

	public void refresh(java.util.List<io.intino.konos.server.activity.displays.schemas.Item> value) {
		putToDisplay("refresh", "value", value);
	}

	public void refreshPageSize(Integer value) {
		putToDisplay("refreshPageSize", "value", value);
	}

	public void refreshItem(io.intino.konos.server.activity.displays.schemas.Item value) {
		putToDisplay("refreshItem", "value", value);
	}

	public void refreshCount(Integer value) {
		putToDisplay("refreshCount", "value", value);
	}

	public void refreshSelection(java.util.List<String> value) {
		putToDisplay("refreshSelection", "value", value);
	}

	public void refreshSortingList(java.util.List<Sorting> value) {
		putToDisplay("refreshSortingList", "value", value);
	}

	public void refreshSelectedSorting(Sorting value) {
		putToDisplay("refreshSelectedSorting", "value", value);
	}

	public void refreshPicture(PictureData value) {
		putToDisplay("refreshPicture", "value", value);
	}
}
