package io.intino.konos.alexandria.activity.displays.builders;

import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.schemas.Item;
import io.intino.konos.alexandria.activity.schemas.ItemValidationRefreshInfo;

public class ItemValidationRefreshInfoBuilder {

	public static ItemValidationRefreshInfo build(String validationMessage, Stamp stamp, Item item) {
		ItemValidationRefreshInfo result = new ItemValidationRefreshInfo().stamp(stamp.name()).message(validationMessage);
		if (item != null) result.item(item.name());
		return result;
	}

}
