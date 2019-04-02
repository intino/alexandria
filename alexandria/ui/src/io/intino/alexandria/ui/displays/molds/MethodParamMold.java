package io.intino.alexandria.ui.displays.molds;

import io.intino.alexandria.UiFrameworkBox;

public class MethodParamMold extends AbstractMethodParamMold<UiFrameworkBox> {
	public boolean addComma;

	public MethodParamMold(UiFrameworkBox box) {
        super(box);
    }

	@Override
	public void init() {
		super.init();
		name.update(parameter.name());
		type.update(parameter.type());
		if (addComma) comma.update(", ");
	}
}