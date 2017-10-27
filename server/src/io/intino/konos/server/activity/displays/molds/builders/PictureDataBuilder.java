package io.intino.konos.server.activity.displays.molds.builders;

import io.intino.konos.server.activity.displays.schemas.PictureData;

import java.util.Base64;

public class PictureDataBuilder {

    public static PictureData build(io.intino.konos.server.activity.displays.elements.model.Item item, String stamp, String value) {
        return new PictureData().item(new String(Base64.getEncoder().encode(item.id().getBytes()))).stamp(stamp).value(value);
    }

}
