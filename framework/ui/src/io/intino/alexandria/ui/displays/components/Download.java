package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.UserMessage;
import io.intino.alexandria.ui.displays.events.operation.DownloadEvent;
import io.intino.alexandria.ui.displays.events.operation.DownloadListener;
import io.intino.alexandria.ui.displays.notifiers.DownloadNotifier;
import io.intino.alexandria.ui.spark.UIFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class Download<DN extends DownloadNotifier, B extends Box> extends AbstractDownload<DN, B> {
    private String option = null;
    private java.util.List<String> options;
    private Collection collection;
    private Formatter formatter;
    private DownloadListener downloadListener;

    private static final String DefaultSeparator = "\t";
    private static final String LineSeparator = "\n";
    private static final String DefaultExtension = ".txt";

    public Download(B box) {
        super(box);
    }

    public List<String> options() {
        return options;
    }

    public Download<DN, B> bindTo(Collection collection) {
        this.collection = collection;
        return this;
    }

    public Download<DN, B> formatter(Formatter formatter) {
        this.formatter = formatter;
        return this;
    }

    public void changeParams(String option) {
        this.option = option;
    }

    public void onExecute(DownloadListener listener) {
        this.downloadListener = listener;
    }

    public UIFile execute() {
        if (collection != null) return executeDownloadCollection();
        else if (this.downloadListener == null) return defaultFile();
        return this.downloadListener.accept(new DownloadEvent(this, option));
    }

    protected Download<DN, B> _options(List<String> options) {
        this.options = options;
        return this;
    }

    protected Download<DN, B> _select(String option) {
        this.option = option;
        return this;
    }

    private UIFile executeDownloadCollection() {
        if (formatter == null) {
            notifyUser(translate("You must implement formatter for downloading collections"), UserMessage.Type.Error);
            return defaultFile();
        }
        notifyUser(translate("Downloading. It might take a few minutes, please, wait..."), UserMessage.Type.Loading);
        return new UIFile() {
            @Override
            public String label() {
                return formatter.filename() != null ? formatter.filename() : downloadDate() + DefaultExtension;
            }

            @Override
            public InputStream content() {
                String content = serializeEntries();
                notifyUser(translate("Downloaded"), UserMessage.Type.Success);
                return new ByteArrayInputStream(content.getBytes());
            }
        };
    }

    @SuppressWarnings({"StreamToLoop", "unchecked"})
    private String serializeEntries() {
        StringBuilder result = new StringBuilder();
        String separator = formatter.separator() != null ? formatter.separator() : DefaultSeparator;
        collection.items(formatter.sortings()).forEach(item -> result.append(serializeEntry(item, separator)));
        return result.toString();
    }

    @SuppressWarnings("unchecked")
    private String serializeEntry(Object item, String separator) {
        StringBuilder result = new StringBuilder();
        formatter.columns(item).forEach(c -> result.append(c).append(separator));
        result.append(LineSeparator);
        return result.toString();
    }

    private String downloadDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("YYYYMMdd");
        return formatter.format(new Date(Instant.now().toEpochMilli()));
    }

    public interface Formatter<T> {
        String filename();
        List<String> columns(T item);
        String[] sortings();

        default String separator() {
            return DefaultSeparator;
        }
    }
}