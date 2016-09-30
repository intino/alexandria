package org.siani.pandora.server;

import org.siani.pandora.exceptions.PandoraException;
import org.siani.pandora.server.pushservice.Client;
import org.siani.pandora.server.pushservice.PushService;
import org.siani.pandora.server.pushservice.Session;
import org.siani.pandora.server.pushservice.SessionProvider;
import org.siani.pandora.server.security.NullSecurityManager;
import org.siani.pandora.server.security.PandoraSecurityManager;
import org.siani.pandora.server.spark.PushServiceHandler;
import org.siani.pandora.server.spark.SparkManager;
import spark.Spark;

import static spark.Spark.webSocket;
import static spark.Spark.webSocketIdleTimeoutMillis;

public class PandoraSpark {

	private PandoraSecurityManager securityManager = new NullSecurityManager();
	private PushService pushService;
	private final String webDirectory;
	private static final int OneDay = 24 * 60 * 60 * 1000;

	public PandoraSpark(int port) {
		this(port, "/web");
	}

	public PandoraSpark(int port, String webDirectory) {
		Spark.port(port);
		configureStaticFiles(webDirectory);
		this.webDirectory = webDirectory;
	}

	public String webDirectory() {
		return webDirectory;
	}

	public void secure(PandoraSecurityManager manager) {
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

		public void before(ResourceCaller caller) {
			Spark.before(path, (rq, rs) -> before(caller, new SparkManager(rq, rs, sessionProvider())));
		}

		public void get(ResourceCaller caller) {
			Spark.get(path, (rq, rs) -> execute(caller, new SparkManager(rq, rs, sessionProvider())));
		}

		public void post(ResourceCaller caller) {
			Spark.post(path, (rq, rs) -> execute(caller, new SparkManager(rq, rs, sessionProvider())));
		}

		public void put(ResourceCaller caller) {
			Spark.put(path, (rq, rs) -> execute(caller, new SparkManager(rq, rs, sessionProvider())));
		}

		public void delete(ResourceCaller caller) {
			Spark.delete(path, (rq, rs) -> execute(caller, new SparkManager(rq, rs, sessionProvider())));
		}

		public void after(ResourceCaller caller) {
			Spark.after(path, (rq, rs) -> after(caller, new SparkManager(rq, rs, sessionProvider())));
		}

		public void push(PushService service) {
			PandoraSpark.this.pushService = service;
			PushServiceHandler.inject(service);
			webSocketIdleTimeoutMillis(OneDay);
			webSocket(path, PushServiceHandler.class);
		}

		private SessionProvider sessionProvider() {
			return new SessionProvider() {
				@Override
				public <S extends Session> S session(String id) {
					if (pushService == null) return null;
					return pushService.session(id);
				}

				@Override
				public <C extends Client> C client(String id) {
					return pushService.client(id);
				}

				@Override
				public <C extends Client> C currentClient() {
					return pushService.currentClient();
				}
			};
		}

		private boolean validRequest(SparkManager manager) {
			return securityManager.check(manager.fromQuery("hash", String.class), manager.fromQuery("signature", String.class));
		}

		private void before(ResourceCaller caller, SparkManager manager) {
			try {
				caller.call(manager);
			} catch (PandoraException e) {
				e.printStackTrace();
			}
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

		private void after(ResourceCaller caller, SparkManager manager) {
			try {
				caller.call(manager);
			} catch (PandoraException e) {
				e.printStackTrace();
			}
		}
	}

	public interface ResourceCaller {
		void call(SparkManager manager) throws PandoraException;
	}

	public void configureStaticFiles(String path) {
		if (isInClasspath(path))
			Spark.staticFileLocation(path);
		else
			Spark.externalStaticFileLocation(path);
	}

	private boolean isInClasspath(String path) {
		return getClass().getClassLoader().getResourceAsStream(path) != null;
	}

}
