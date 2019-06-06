package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.events.operation.DownloadListener;
import io.intino.alexandria.ui.spark.UIFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class DownloadExamplesMold extends AbstractDownloadExamplesMold<AlexandriaUiBox> {

    public DownloadExamplesMold(AlexandriaUiBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        download1.onExecute(exampleFile());
        download2.onExecute(exampleFile());
    }

    private DownloadListener exampleFile() {
        return (event) -> new UIFile() {
            @Override
            public String label() {
                return "example" + (!event.option().isEmpty() ? " - " + event.option() : "") + ".pdf";
            }

            @Override
            public InputStream content() {
                return new ByteArrayInputStream(new byte[0]);
            }
        };
    }
}