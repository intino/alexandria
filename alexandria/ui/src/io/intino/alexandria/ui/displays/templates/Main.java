package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.blocks.TextBlock;

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
        updateTextBlock();
//        int[] counter = new int[1];
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                panels.textPanel.valueC.update("valor C " + counter[0]);
//                counter[0]++;
//            }
//        }, 1000, 1000);
    }

    private void updateTextBlock() {
        TextBlock textBlock = panels.textPanel.textBlock;
        textBlock.valueA.update("abcd");
        textBlock.valueB.update("efgh");
        textBlock.valueC.update("ijkl");
        textBlock.valueC.onChange((e) -> System.out.println((String) e.value()));
    }

}