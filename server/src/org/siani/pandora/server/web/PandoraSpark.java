package org.siani.pandora.server.web;

import org.siani.pandora.exceptions.PandoraException;
import org.siani.pandora.server.security.NullSecurityManager;
import org.siani.pandora.server.security.TeseoSecurityManager;
import org.siani.pandora.server.services.PushService;
import org.siani.pandora.server.web.services.PushServiceHandler;
import org.siani.pandora.server.web.spark.SparkManager;
import spark.Spark;

import static spark.Spark.webSocket;
import static spark.Spark.webSocketIdleTimeoutMillis;


public class PandoraSpark {

	private TeseoSecurityManager securityManager = new NullSecurityManager();
	private final String webDirectory;
	private static final int OneDay = 24 * 60 * 60 * 1000;

	public PandoraSpark(int port) {
		this(port, "/web");
	}

	public PandoraSpark(int port, String webDirectory) {
		Spark.port(port);
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
			return securityManager.check(manager.fromQuery("hash", String.class), manager.fromQuery("signature", String.class));
		}

		private Object execute(ResourceCaller caller, SparkManager manager) {
			if (!validRequest(manager)) return "FAILURE";
			try {
				caller.call(manager);
			} catch (PandoraException e) {
				return e.code();
			}
			return "OK";
		}
	}

	public interface ResourceCaller {

		void call(SparkManager manager) throws PandoraException;

	}

}
