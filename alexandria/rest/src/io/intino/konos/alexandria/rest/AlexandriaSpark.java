package io.intino.konos.alexandria.rest;

import io.intino.konos.alexandria.exceptions.AlexandriaException;
import io.intino.konos.alexandria.rest.security.AlexandriaSecurityManager;
import io.intino.konos.alexandria.rest.security.NullSecurityManager;
import io.intino.konos.alexandria.rest.spark.PushService;
import io.intino.konos.alexandria.rest.spark.SparkManager;
import io.intino.konos.alexandria.rest.spark.SparkRouter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.ThreadPool;
import spark.Service;
import spark.embeddedserver.EmbeddedServers;
import spark.embeddedserver.jetty.EmbeddedJettyFactory;
import spark.embeddedserver.jetty.JettyServerFactory;

import java.util.function.Consumer;
import java.util.function.Function;

public class AlexandriaSpark<R extends SparkRouter> {
	private AlexandriaSecurityManager securityManager = new NullSecurityManager();
	protected final String webDirectory;
	protected PushService pushService;
	protected static final String WebDirectory = "/www";
	protected Service service;
	protected int port;

	static {
		CustomJettyServerFactory customJettyServerFactory = new CustomJettyServerFactory();
		EmbeddedServers.add(EmbeddedServers.Identifiers.JETTY, new EmbeddedJettyFactory(customJettyServerFactory));
	}

	public AlexandriaSpark(int port) {
		this(port, WebDirectory);
	}

	public AlexandriaSpark(int port, String webDirectory) {
		this.port = port;
		this.webDirectory = webDirectory;
		this.service = Service.ignite();
		configureStaticFiles();
		service.port(this.port);
	}

	public AlexandriaSpark start() {
		service.init();
		return this;
	}

	public void stop() {
		if (service != null) service.stop();
		service = null;
	}

	public String webDirectory() {
		return webDirectory;
	}

	public void secure(AlexandriaSecurityManager manager) {
		this.securityManager = manager;
	}

	public R route(String path) {
		R router = createRouter(path);
		router.inject(pushService);

		router.whenRegisterPushService(new Consumer<PushService>() {
			@Override
			public void accept(PushService pushService) {
				AlexandriaSpark.this.pushService = pushService;
			}
		});

		router.whenValidate(new Function<SparkManager, Boolean>() {
			@Override
			public Boolean apply(SparkManager manager) {
				return securityManager.check(manager.fromQuery("hash", String.class), manager.fromQuery("signature", String.class));
			}
		});

		return router;
	}

	protected R createRouter(String path) {
		return (R) new SparkRouter(service, path);
	}

	public interface ResourceCaller<SM extends SparkManager> {
		void call(SM manager) throws AlexandriaException;
	}

	protected void configureStaticFiles() {
		if (isInClasspath(webDirectory)) service.staticFileLocation(webDirectory);
		else service.externalStaticFileLocation(webDirectory);
	}

	static class CustomJettyServerFactory implements JettyServerFactory {
		@Override
		public Server create(int maxThreads, int minThreads, int threadTimeoutMillis) {
			Server server = new Server();
			server.setAttribute("org.eclipse.jetty.server.Request.maxFormContentSize", 1000000);
			return server;
		}

		@Override
		public Server create(ThreadPool threadPool) {
			return null;
		}
	}

	private boolean isInClasspath(String path) {
		return getClass().getClassLoader().getResourceAsStream(path) != null;
	}

}
