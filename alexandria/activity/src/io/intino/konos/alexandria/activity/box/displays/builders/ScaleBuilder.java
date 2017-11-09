package io.intino.konos.alexandria.activity.box.displays.builders;

import io.intino.konos.alexandria.activity.box.model.TimeScale;
import io.intino.konos.alexandria.activity.box.schemas.Scale;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ScaleBuilder {

    public static Scale build(TimeScale timeScale, String language) {
        return new Scale().label(timeScale.label())
                .name(timeScale.name())
                .symbol(timeScale.symbol(language));
    }

    public static List<Scale> buildList(List<TimeScale> timeScaleList, String language) {
        return timeScaleList.stream().map(scale -> build(scale, language)).collect(toList());
    }

}
