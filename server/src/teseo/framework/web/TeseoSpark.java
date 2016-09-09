package teseo.framework.web;

import spark.Spark;
import teseo.exceptions.TeseoException;
import teseo.framework.security.NullSecurityManager;
import teseo.framework.security.TeseoSecurityManager;
import teseo.framework.services.PushService;
import teseo.framework.web.services.PushServiceHandler;

import static spark.Spark.webSocket;
import static spark.Spark.webSocketIdleTimeoutMillis;


public class TeseoSpark {

	private final boolean secure;
	private TeseoSecurityManager securityManager = new NullSecurityManager();
	private final String webDirectory;
	private static final int OneDay = 24 * 60 * 60 * 1000;

	public TeseoSpark(int port, boolean secure) {
		this(port, secure, "/web");
	}

	public TeseoSpark(int port, boolean secure, String webDirectory) {
		Spark.port(port);
		this.secure = secure;
		this.webDirectory = webDirectory;
	}

	public String webDirectory() {
		return webDirectory;
	}

	public void secure(TeseoSecurityManager manager) {
		this.securityManager = manager;
	}

	public Router route(String path) {
		return new Router(path);
	}

	public class Router {

		private final String path;

		public Router(String path) {
			this.path = path;
		}

		public void get(ResourceCaller caller) {
			Spark.get(path, (rq, rs) -> execute(caller, new SparkManager(rq, rs)));
		}

		public void post(ResourceCaller caller) {
			Spark.post(path, (rq, rs) -> execute(caller, new SparkManager(rq, rs)));
		}

		public void put(ResourceCaller caller) {
			Spark.put(path, (rq, rs) -> execute(caller, new SparkManager(rq, rs)));
		}

		public void delete(ResourceCaller caller) {
			Spark.delete(path, (rq, rs) -> execute(caller, new SparkManager(rq, rs)));
		}

		public void push(PushService service) {
			PushServiceHandler.inject(service);
			webSocketIdleTimeoutMillis(OneDay);
			webSocket(path, PushServiceHandler.class);
		}

		private boolean validRequest(SparkManager manager) {
			return !secure || securityManager.check(manager.fromQuery("hash", String.class), manager.fromQuery("signature", String.class));
		}

		private Object execute(ResourceCaller caller, SparkManager manager) {
			if (!validRequest(manager)) return "FAILURE";
			try {
				caller.call(manager);
			} catch (TeseoException e) {
				return e.code();
			}
			return "OK";
		}
	}

	public interface ResourceCaller {

		void call(SparkManager manager) throws TeseoException;

	}

}
