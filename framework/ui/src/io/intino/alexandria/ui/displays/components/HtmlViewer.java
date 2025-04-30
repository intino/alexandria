package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.HtmlViewerOperation;
import io.intino.alexandria.ui.displays.notifiers.HtmlViewerNotifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class HtmlViewer<DN extends HtmlViewerNotifier, B extends Box> extends AbstractHtmlViewer<B> {
    private String content;
    private final java.util.Map<String, Consumer<java.util.List<String>>> operationListeners = new HashMap<>();

    public HtmlViewer(B box) {
        super(box);
    }

    public HtmlViewer<DN, B> content(String content) {
        this.content = content;
        return this;
    }

    public HtmlViewer<DN, B> on(String operation, Consumer<java.util.List<String>> listener) {
        this.operationListeners.put(operation, listener);
        return this;
    }

    public void print() {
        print(null);
    }

    public void print(String title) {
        notifier.print(title);
    }

    public void execute(HtmlViewerOperation operation) {
        if (!operationListeners.containsKey(operation.name())) return;
        operationListeners.get(operation.name()).accept(operation.params());
    }

    @Override
    public void refresh() {
        super.refresh();
        notifier.refresh(content);
        if (!operationListeners.isEmpty()) notifier.refreshOperations(new ArrayList<>(operationListeners.keySet()));
    }
}