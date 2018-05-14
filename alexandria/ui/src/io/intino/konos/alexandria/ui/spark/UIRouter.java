package io.intino.konos.alexandria.ui.spark;

import io.intino.konos.alexandria.rest.spark.SparkRouter;
import io.intino.konos.alexandria.ui.services.AuthService;
import io.intino.konos.alexandria.ui.services.EditorService;
import io.intino.konos.alexandria.ui.services.push.PushService;
import spark.Request;
import spark.Response;
import spark.Service;

public class UIRouter extends SparkRouter<UISparkManager> {
	protected final AuthService authService;
	protected final EditorService editorService;
	private static boolean hasUserHome = false;

	public UIRouter(Service service, String path, AuthService authService, EditorService editorService) {
		super(service, path);
		if (isUserHomePath(path)) hasUserHome = true;
		this.authService = authService;
		this.editorService = editorService;
	}

	private boolean isUserHomePath(String path) {
		return path.contains(UISparkManager.KonosUserHomePath);
	}

	@Override
	protected UISparkManager manager(Request rq, Response rs) {
		return new UISparkManager(rq, rs, (PushService) pushService, authService, editorService, hasUserHome);
	}

}
