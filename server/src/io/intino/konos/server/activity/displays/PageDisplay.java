package io.intino.konos.server.activity.displays;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class PageDisplay<N extends DisplayNotifier> extends Display<N> {
    private int page;
    private int pageSize;
    private List<Consumer<List<String>>> selectListeners = new ArrayList<>();

    private static final int PageSize = 20;

    public PageDisplay() {
        super();
        this.page = 0;
        this.pageSize = PageSize;
    }

    public void addSelectListener(Consumer<List<String>> listener) {
        selectListeners.add(listener);
    }

    public int page() {
        return this.page;
    }

    public void page(int index) {
        this.page = index;
        this.checkPageRange();
        this.sendItems(start(), limit());
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

    public void lastPage() {
        page(countPages()-1);
    }

    public int countPages() {
        long countItems = countItems();
        return Math.round(countItems / pageSize) + (countItems % pageSize > 0 ? 1 : 0);
    }

    @Override
    public void refresh() {
        sendClear();
        sendCount(countItems());
        page(0);
    }

    public abstract int countItems();

    @Override
    protected void init() {
        super.init();
        sendCount(countItems());
    }

    protected abstract void sendItems(int start, int limit);
    protected abstract void sendClear();
    protected abstract void sendCount(int count);

    protected void notifySelectListeners(List<String> selection) {
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
