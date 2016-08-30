package teseo.framework.web;

import teseo.framework.actions.Router;
import teseo.framework.web.actions.SparkRouter;

public class SparkWebServer extends TeseoServer {
	private final int port;
	private final Router router;

	public SparkWebServer(int port) {
		this.port = port;
		this.router = this.createRouter();
	}

	public SparkWebServer(int port, String webDirectory) {
		super(webDirectory);
		this.port = port;
		this.router = this.createRouter();
	}

	@Override
	protected Router router() {
		return router;
	}

	private Router createRouter() {
		Router router = new SparkRouter(port, this);
		router.staticFiles(webDirectory());
		return router;
	}

}
