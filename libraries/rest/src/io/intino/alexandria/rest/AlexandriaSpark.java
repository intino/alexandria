package io.intino.alexandria.rest;

import io.intino.alexandria.exceptions.AlexandriaException;
import io.intino.alexandria.rest.security.AlexandriaSecurityManager;
import io.intino.alexandria.rest.security.NullSecurityManager;
import io.intino.alexandria.rest.spark.PushService;
import io.intino.alexandria.rest.spark.SparkManager;
import io.intino.alexandria.rest.spark.SparkRouter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.ThreadPool;
import spark.Service;
import spark.embeddedserver.EmbeddedServers;
import spark.embeddedserver.jetty.EmbeddedJettyServer;
import spark.embeddedserver.jetty.JettyHandler;
import spark.embeddedserver.jetty.JettyServerFactory;
import spark.http.matching.MatcherFilter;
import spark.route.Routes;
import spark.staticfiles.StaticFilesConfiguration;

import java.util.function.Consumer;
import java.util.function.Function;

public class AlexandriaSpark<R extends SparkRouter> {
	private AlexandriaSecurityManager securityManager = new NullSecurityManager();
	protected final String webDirectory;
	protected PushService pushService;
	protected static final String WebDirectory = "/www";
	protected Service service;
	protected int port;

	public AlexandriaSpark(int port) {
		this(port, WebDirectory);
	}

	public AlexandriaSpark(int port, String webDirectory) {
		this.port = port;
		this.webDirectory = webDirectory;
		this.service = Service.ignite();
		setup();
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
		router.whenRegisterPushService(pushServiceConsumer());
		router.whenValidate((Function<SparkManager<?>, Boolean>) manager -> securityManager.check(manager.fromQuery("hash", String.class), manager.fromQuery("signature", String.class)));
		return router;
	}

	private Consumer<PushService> pushServiceConsumer() {
		return (pushService) -> AlexandriaSpark.this.pushService = pushService;
	}

	protected R createRouter(String path) {
		return (R) new SparkRouter(service, path);
	}

	public interface ResourceCaller<SM extends SparkManager> {
		void call(SM manager) throws AlexandriaException;
	}

	protected void setupStaticFiles() {
		if (isInClasspath(webDirectory)) service.staticFileLocation(webDirectory);
		else service.externalStaticFileLocation(webDirectory);
	}

	private boolean isInClasspath(String path) {
		return getClass().getClassLoader().getResourceAsStream(path) != null;
	}

	private void setup() {
		setupRequest();
		setupStaticFiles();
	}

	private void setupRequest() {
		EmbeddedServers.add(EmbeddedServers.Identifiers.JETTY, (Routes routeMatcher, StaticFilesConfiguration staticFilesConfiguration, boolean hasMultipleHandler) -> {
			JettyHandler handler = setupRequestHandler(routeMatcher, staticFilesConfiguration, hasMultipleHandler);
			return new EmbeddedJettyServer(new JettyServerFactory() {
				@Override
				public Server create(int i, int i1, int i2) {
					return server();
				}

				@Override
				public Server create(ThreadPool threadPool) {
					return server();
				}

				private Server server() {
					Server result = new Server();
					result.setAttribute("org.eclipse.jetty.server.Request.maxFormContentSize",100*1024*1024); // bytes
					return result;
				}

			}, handler);
		});
	}

	private static JettyHandler setupRequestHandler(Routes routeMatcher, StaticFilesConfiguration staticFilesConfiguration, boolean hasMultipleHandler) {
		MatcherFilter matcherFilter = new MatcherFilter(routeMatcher, staticFilesConfiguration, false, hasMultipleHandler);
		matcherFilter.init(null);
		return new JettyHandler(matcherFilter);
	}
}
