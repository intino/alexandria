package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.ui.AlexandriaUiBox;

public class ItemMold extends AbstractItemMold<AlexandriaUiBox> {

    public ItemMold(AlexandriaUiBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
//        refresh();
//        int[] count = { 1 };
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                label.update(item().label() + ". Iteration " + count[0]);
//                count[0]++;
//            }
//        }, 1000, 1000);
    }

    @Override
    public void refresh() {
        String label = item().label();
        avatar.update(label);
        this.label.update(label);
    }

}