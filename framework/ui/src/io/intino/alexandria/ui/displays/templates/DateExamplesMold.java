package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.UserMessage;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateExamplesMold extends AbstractDateExamplesMold<AlexandriaUiBox> {

    public DateExamplesMold(AlexandriaUiBox box) {
        super(box);
    }

    private static final SimpleDateFormat Formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void init() {
        super.init();
        date1.value(Instant.now());
        date2.value(Instant.now());
        date3.range(Instant.now(), Instant.now().plus(2, ChronoUnit.DAYS));
        date3.onChange(e -> {
            Instant value = e.value();
            notifyUser(value != null ? Formatter.format(Date.from(value)) : "Not defined", UserMessage.Type.Info);
        });
    }
}