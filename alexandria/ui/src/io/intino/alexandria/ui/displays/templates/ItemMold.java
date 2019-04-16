package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;

import java.util.Timer;
import java.util.TimerTask;

public class ItemMold extends AbstractItemMold<UiFrameworkBox> {

    public ItemMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        refresh();
        int[] count = { 1 };
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                label.update(item.label() + ". Iteration " + count[0]);
                count[0]++;
            }
        }, 1000, 1000);
    }

    @Override
    public void refresh() {
        avatar.update(item.label());
        label.update(item.label());
    }

}