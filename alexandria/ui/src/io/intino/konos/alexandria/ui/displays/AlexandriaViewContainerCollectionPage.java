package io.intino.konos.alexandria.ui.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.ui.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;

public abstract class AlexandriaViewContainerCollectionPage<N extends AlexandriaDisplayNotifier> extends AlexandriaViewContainerCollection<N> {
    private int page;
    private int pageSize;
    private List<Consumer<List<String>>> selectListeners = new ArrayList<>();
    private List<String> selection = new ArrayList<>();

    protected static final int PageSize = 20;

    public AlexandriaViewContainerCollectionPage(Box box) {
        super(box);
        this.page = 0;
        this.pageSize = PageSize;
    }

    @Override
    public List<Item> selectedItems() {
        return selection().stream().map(this::itemOf).collect(toList());
    }

    public List<String> selection() {
        return selection;
    }

    public AlexandriaViewContainerCollectionPage selection(List<String> selection) {
        this.selection = selection;
        if (selection.size() > 0) notifySelectListeners(selection);
        return this;
    }

    public void onSelectItems(Consumer<List<String>> listener) {
        selectListeners.add(listener);
    }

    public int page() {
        return this.page;
    }

    public synchronized void page(int index) {
        this.page = index;
        this.checkPageRange();
        this.sendItems(start(), limit());
        if (isNearToEnd()) {
            int count = countItems();
            notifyNearToEnd();
            if (count < PageSize && countItems() > PageSize) refresh();
            else sendCount(countItems());
        }
    }

    public void pageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void previousPage() {
        page(page--);
    }

    public void nextPage() {
        page(page++);
    }

    public void firstPage() {
        page(0);
    }

    public boolean isNearToEnd() {
        return page >= countPages()-2;
    }

    public void lastPage() {
        page(countPages()-1);
    }

    public int countPages() {
        long countItems = countItems();
        return (int) (Math.floor(countItems / pageSize) + (countItems % pageSize > 0 ? 1 : 0));
    }

    @Override
    public void refresh() {
        notifyLoading(true);
        sendClear();
        sendCount(countItems());
        page(0);
        notifyLoading(false);
    }

    @Override
    protected void init() {
        super.init();
        sendPageSize(this.pageSize);
    }

    public abstract int countItems();
    protected abstract void sendItems(int start, int limit);
    protected abstract void sendClear();
    protected abstract void sendPageSize(int pageSize);
    protected abstract void sendCount(int count);
    protected abstract void notifyNearToEnd();

    private void notifySelectListeners(List<String> selection) {
        selectListeners.forEach(listener -> listener.accept(selection));
    }

    private void checkPageRange() {
        if (this.page <= 0)
            this.page = 0;

        int countPages = countPages();
        if (this.page >= countPages && countPages > 0)
            this.page = countPages - 1;
    }

    private int start() {
        return this.page * this.pageSize;
    }

    private int limit() {
        return this.pageSize;
    }

}
