package io.intino.alexandria.http;

import io.intino.alexandria.exceptions.AlexandriaException;
import io.intino.alexandria.http.security.AlexandriaSecurityManager;
import io.intino.alexandria.http.security.NullSecurityManager;
import io.intino.alexandria.http.spark.PushService;
import io.intino.alexandria.http.spark.SparkManager;
import io.intino.alexandria.http.spark.SparkRouter;
import io.intino.alexandria.logger.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.ThreadPool;
import spark.ExceptionHandler;
import spark.ExceptionMapper;
import spark.Service;
import spark.Spark;
import spark.embeddedserver.EmbeddedServer;
import spark.embeddedserver.EmbeddedServerFactory;
import spark.embeddedserver.EmbeddedServers;
import spark.embeddedserver.jetty.EmbeddedJettyServer;
import spark.embeddedserver.jetty.JettyHandler;
import spark.embeddedserver.jetty.JettyServerFactory;
import spark.http.matching.MatcherFilter;
import spark.route.Routes;
import spark.staticfiles.StaticFilesConfiguration;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AlexandriaSpark<R extends SparkRouter> {
	private AlexandriaSecurityManager securityManager = new NullSecurityManager();
	protected final String webDirectory;
	protected PushService pushService;
	protected static final String WebDirectory = "/www";
	protected Service service;
	protected int port;
	protected ConnectionListener connectionListener;

	static {
		Spark.initExceptionHandler(Logger::error);
	}

	public AlexandriaSpark(int port) {
		this(port, WebDirectory);
	}

	public AlexandriaSpark(int port, String webDirectory) {
		this.port = port;
		this.webDirectory = webDirectory;
		this.service = Service.ignite();
		setup();
		service.port(this.port);
		service.exception(Exception.class, (exception, request, response) -> Logger.error(exception));
	}

	public AlexandriaSpark start() {
		service.init();
		return this;
	}

	public Service service() {
		return service;
	}

	public int activeThreadCount() {
		return service.activeThreadCount();
	}

	public ConnectionListener connectionListener() {
		return connectionListener;
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
		router.whenValidate((Function<SparkManager<?>, Boolean>) manager -> securityManager.check(manager.fromQuery("hash"), manager.fromQuery("signature")));
		return router;
	}


	public <T extends Exception> void handle(Class<T> exceptionClass, ExceptionHandler<? super T> handler) {
		service.exception(exceptionClass, handler);
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
		setupService();
		setupStaticFiles();
	}

	private void setupService() {
		EmbeddedServers.add(EmbeddedServers.Identifiers.JETTY, new EmbeddedServerFactory() {
			@Override
			public EmbeddedServer create(Routes routes, StaticFilesConfiguration staticFilesConfiguration, boolean hasMultipleHandler) {
				return create(routes, staticFilesConfiguration, new ExceptionMapper(), hasMultipleHandler);
			}

			@Override
			public EmbeddedServer create(Routes routes, StaticFilesConfiguration staticFilesConfiguration, ExceptionMapper exceptionMapper, boolean hasMultipleHandler) {
				JettyHandler handler = setupRequestHandler(routes, staticFilesConfiguration, exceptionMapper, hasMultipleHandler);
				return new EmbeddedJettyServer(new JettyServerFactory() {
					@Override
					public Server create(int maxThreads, int minThreads, int threadTimeoutMillis) {
						return server();
					}

					@Override
					public Server create(ThreadPool threadPool) {
						return server();
					}

					private Server server() {
						Server newServer = new Server();
						newServer.setAttribute("org.eclipse.jetty.server.Request.maxFormContentSize", 100 * 1024 * 1024); // bytes
						connectionListener = new ConnectionListener(newServer);
						return newServer;
					}

				}, handler);
			}
		});
	}

	private static JettyHandler setupRequestHandler(Routes routeMatcher, StaticFilesConfiguration staticFilesConfiguration, ExceptionMapper exceptionMapper, boolean hasMultipleHandler) {
		MatcherFilter matcherFilter = new MatcherFilter(routeMatcher, staticFilesConfiguration, exceptionMapper, false, hasMultipleHandler);
		matcherFilter.init(null);
		return new JettyHandler(matcherFilter);
	}

	public static class ConnectionListener {
		private final Server server;

		ConnectionListener(Server server) {
			this.server = server;
		}

		public List<String> currentConnectionSourceIps() {
			return server.getConnectors()[0].getConnectedEndPoints().stream().map(e -> new String(e.getRemoteAddress().getAddress().getAddress())).collect(Collectors.toList());
		}
	}
}
