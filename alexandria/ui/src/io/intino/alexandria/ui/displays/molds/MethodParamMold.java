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
		name.update(methodParameter.name());
		type.update(methodParameter.type());
		if (addComma) comma.update(", ");
	}
}