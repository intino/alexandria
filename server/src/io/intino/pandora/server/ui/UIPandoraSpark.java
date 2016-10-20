package io.intino.pandora.server.ui;

import io.intino.pandora.server.PandoraSpark;
import io.intino.pandora.server.ui.spark.UIRouter;

public class UIPandoraSpark extends PandoraSpark<UIRouter> {

	public UIPandoraSpark(int port) {
		super(port);
	}

	public UIPandoraSpark(int port, String webDirectory) {
		super(port, webDirectory);
	}

	@Override
	protected UIRouter createRouter(String path) {
		return new UIRouter(path);
	}

}
