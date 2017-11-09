package io.intino.konos.alexandria.activity.box.displays.builders;

import io.intino.konos.alexandria.activity.box.schemas.PictureData;

import java.util.Base64;

public class PictureDataBuilder {

    public static PictureData build(io.intino.konos.alexandria.activity.box.model.Item item, String stamp, String value) {
        return new PictureData().item(new String(Base64.getEncoder().encode(item.id().getBytes()))).stamp(stamp).value(value);
    }

}
