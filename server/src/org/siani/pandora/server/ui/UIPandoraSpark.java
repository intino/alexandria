package org.siani.pandora.server.ui;

import org.siani.pandora.server.PandoraSpark;
import org.siani.pandora.server.ui.spark.UIRouter;

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
