package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;

public class Main extends AbstractMain<UiFrameworkBox> {

    public Main(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        updateTextPanel();
    }

    private void updateTextPanel() {
        title.update("Alexandria widgets");
//        panels.textPanel.valueA.update("abcd");
//        panels.textPanel.valueB.update("efgh");
//        panels.textPanel.valueC.update("ijkl");
//        panels.textPanel.valueC.onChange((e) -> System.out.println((String) e.value()));
//        int[] counter = new int[1];
//
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                panels.textPanel.valueC.update("valor C " + counter[0]);
//                counter[0]++;
//            }
//        }, 1000, 1000);
    }
}