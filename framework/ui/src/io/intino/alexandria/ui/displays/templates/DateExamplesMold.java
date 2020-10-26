package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.ui.AlexandriaUiBox;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class DateExamplesMold extends AbstractDateExamplesMold<AlexandriaUiBox> {

    public DateExamplesMold(AlexandriaUiBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        date3.range(Instant.now(), Instant.now().plus(2, ChronoUnit.DAYS));
    }
}