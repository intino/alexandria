package io.intino.alexandria.ui.displays;

import io.intino.alexandria.ui.AlexandriaUiBox;

public class DisplayRouter<B extends AlexandriaUiBox> extends AbstractDisplayRouter<B> {

    public DisplayRouter(B box) {
        super(box);
    }

	public void dispatch(String address) {
		box().routeManager().routeDispatcher().dispatch(soul(), address);
	}
}