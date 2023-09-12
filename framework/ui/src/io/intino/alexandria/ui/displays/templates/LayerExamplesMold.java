package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.UserMessage;
import io.intino.alexandria.ui.displays.components.Layer;

public class LayerExamplesMold extends AbstractLayerExamplesMold<UiFrameworkBox> {

    public LayerExamplesMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        openLayerTrigger.onOpen(e -> {
            Layer<?, ?> layer = e.layer();
            layer.title("This is a layer example");
            layer.template(new LayerExamplesMold(box()));
            layer.onExecuteOperation(name -> notifyUser("Ejecutar la operación " + name, UserMessage.Type.Info));
            layer.addOperation("aaa", "Home");
            layer.refresh();
        });
    }
}