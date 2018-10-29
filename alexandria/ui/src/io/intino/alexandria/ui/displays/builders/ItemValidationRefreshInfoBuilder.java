package io.intino.alexandria.ui.displays.builders;

import io.intino.alexandria.ui.model.mold.Stamp;
import io.intino.konos.alexandria.ui.schemas.Item;
import io.intino.konos.alexandria.ui.schemas.ItemValidationRefreshInfo;

public class ItemValidationRefreshInfoBuilder {

	public static ItemValidationRefreshInfo build(String validationMessage, Stamp stamp, Item item) {
		ItemValidationRefreshInfo result = new ItemValidationRefreshInfo().stamp(stamp.name()).message(validationMessage);
		if (item != null) result.item(item.name());
		return result;
	}

}
