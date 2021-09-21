package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.HtmlViewerNotifier;

public class HtmlViewer<DN extends HtmlViewerNotifier, B extends Box> extends AbstractHtmlViewer<B> {
    private String content;

    public HtmlViewer(B box) {
        super(box);
    }

    public HtmlViewer<DN, B> content(String content) {
        this.content = content;
        return this;
    }

    @Override
    public void refresh() {
        super.refresh();
        notifier.refresh(content);
    }

}