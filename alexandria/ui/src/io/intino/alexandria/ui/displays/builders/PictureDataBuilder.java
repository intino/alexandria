package io.intino.alexandria.ui.displays.builders;

import io.intino.alexandria.ui.model.Item;
import io.intino.konos.alexandria.ui.schemas.PictureData;

import java.util.Base64;

public class PictureDataBuilder {

    public static PictureData build(Item item, String stamp, String value) {
        return new PictureData().item(new String(Base64.getEncoder().encode(item.id().getBytes()))).stamp(stamp).value(value);
    }

}
