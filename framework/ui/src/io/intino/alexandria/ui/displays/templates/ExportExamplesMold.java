package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.events.actionable.ExportEvent;
import io.intino.alexandria.ui.displays.events.actionable.ExportListener;
import io.intino.alexandria.ui.spark.UIFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ExportExamplesMold extends AbstractExportExamplesMold<AlexandriaUiBox> {

    public ExportExamplesMold(AlexandriaUiBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        export1.onExecute(exampleFile());
        export2.onExecute(exampleFile());
        export3.onExecute(exampleFile());
    }

    private ExportListener exampleFile() {
        return (event) -> new UIFile() {
            @Override
            public String label() {
                return format(event) + ".pdf";
            }

            @Override
            public InputStream content() {
                return new ByteArrayInputStream(new byte[0]);
            }
        };
    }

    private String format(ExportEvent event) {
        return "example " + event.from() + "-" + event.to() + (event.option() != null && !event.option().isEmpty() ? "-" + event.option() : "");
    }
}