package io.intino.pandora.server.activity;

import io.intino.pandora.server.PandoraSpark;
import io.intino.pandora.server.activity.spark.ActivityRouter;

public class ActivityPandoraSpark extends PandoraSpark<ActivityRouter> {

	public ActivityPandoraSpark(int port) {
		super(port);
	}

	public ActivityPandoraSpark(int port, String webDirectory) {
		super(port, webDirectory);
	}

	@Override
	protected ActivityRouter createRouter(String path) {
		return new ActivityRouter(path);
	}

}
