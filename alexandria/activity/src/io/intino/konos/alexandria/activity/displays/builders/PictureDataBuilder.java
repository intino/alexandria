package io.intino.konos.alexandria.activity.displays.builders;

import io.intino.konos.alexandria.activity.schemas.PictureData;
import io.intino.konos.alexandria.activity.model.Item;

import java.util.Base64;

public class PictureDataBuilder {

    public static PictureData build(Item item, String stamp, String value) {
        return new PictureData().item(new String(Base64.getEncoder().encode(item.id().getBytes()))).stamp(stamp).value(value);
    }

}
