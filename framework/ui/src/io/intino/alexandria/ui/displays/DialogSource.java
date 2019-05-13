package io.intino.alexandria.ui.displays;

import io.intino.alexandria.ui.model.Dialog;

import java.util.List;

public interface DialogSource {
    List<String> options(Dialog.Tab.Input input);
}
