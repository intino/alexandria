package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.UserMessage;
import io.intino.alexandria.ui.displays.events.collection.AddItemEvent;
import io.intino.alexandria.ui.displays.items.DownloadSelectionMold;
import io.intino.alexandria.ui.documentation.Person;
import io.intino.alexandria.ui.documentation.model.Datasources;
import io.intino.alexandria.ui.model.datasource.PageDatasource;
import io.intino.alexandria.ui.spark.UIFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class DownloadSelectionExamplesMold extends AbstractDownloadSelectionExamplesMold<AlexandriaUiBox> {

    public DownloadSelectionExamplesMold(AlexandriaUiBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        init(list6, Datasources.personDatasource());
        download.onExecute((event) -> {
            download.notifyUser("download " + event.selection().size() + " items ", UserMessage.Type.Info);
            return new UIFile() {
                @Override
                public String label() {
                    return "download" + (!event.option().isEmpty() ? " - " + event.option() : "") + ".pdf";
                }

                @Override
                public InputStream content() {
                    return new ByteArrayInputStream(new byte[0]);
                }
            };
        });
    }

    private void init(io.intino.alexandria.ui.displays.components.List list, PageDatasource datasource) {
        list.source(datasource);
        list.onAddItem(this::onAddItem);
    }

    private void onAddItem(AddItemEvent event) {
        if (event.component() instanceof DownloadSelectionMold) ((DownloadSelectionMold) event.component()).firstName.value(((Person) event.item()).firstName());
    }

}