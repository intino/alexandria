package io.intino.alexandria.foundation.activity.dialogs;

import io.intino.alexandria.foundation.activity.model.Dialog;

import java.util.List;

public interface DialogSource {
    List<String> options(Dialog.Tab.Input input);
}
