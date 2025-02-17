package io.intino.alexandria.http;

import io.intino.alexandria.exceptions.AlexandriaException;
import io.intino.alexandria.http.javalin.JavalinHttpRouter;
import io.intino.alexandria.http.pushservice.PushService;
import io.intino.alexandria.http.security.AlexandriaSecurityManager;
import io.intino.alexandria.http.security.NullSecurityManager;
import io.intino.alexandria.http.server.AlexandriaHttpManager;
import io.intino.alexandria.http.server.AlexandriaHttpRouter;
import io.intino.alexandria.logger.Logger;
import io.javalin.Javalin;
import io.javalin.config.StaticFilesConfig;
import io.javalin.http.Context;
import io.javalin.http.ExceptionHandler;
import io.javalin.http.staticfiles.Location;
import io.javalin.http.staticfiles.ResourceHandler;
import io.javalin.http.staticfiles.StaticFileConfig;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.function.Consumer;

public class AlexandriaHttpServer<R extends AlexandriaHttpRouter<?>> {
	private AlexandriaSecurityManager securityManager = new NullSecurityManager();
	protected final String webDirectory;
	protected PushService<?, ?> pushService;
	protected Javalin service;
	protected int port;
	private boolean started = false;

	protected static final String WebDirectory = "/www";
	protected static final long MaxResourceSize = 100 * 1024 * 1024; // 100MB

	public AlexandriaHttpServer(int port) {
		this(port, WebDirectory);
	}

	public AlexandriaHttpServer(int port, String webDirectory) {
		this(port, webDirectory, MaxResourceSize);
	}

	public AlexandriaHttpServer(int port, String webDirectory, long maxResourceSize) {
		this.port = port;
		this.webDirectory = webDirectory;
		this.service = create(webDirectory, maxResourceSize);
		service.exception(Exception.class, (exception, context) -> Logger.error(exception));
	}

	public AlexandriaHttpServer<?> start() {
		if (started) return this;
		service.start(port);
		started = true;
		return this;
	}

	public int activeThreadCount() {
		return service.jettyServer().threadPool().getThreads();
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
		router.push(pushService);
		router.whenRegisterPushService(pushServiceConsumer());
		router.whenValidate(manager -> securityManager.check(manager.fromQuery("hash"), manager.fromQuery("signature")));
		return router;
	}

	public <T extends Exception> void handle(Class<T> exceptionClass, ExceptionHandler<? super T> handler) {
		service.exception(exceptionClass, handler);
	}

	private Consumer<PushService<?, ?>> pushServiceConsumer() {
		return (pushService) -> AlexandriaHttpServer.this.pushService = pushService;
	}

	protected R createRouter(String path) {
		return (R) new JavalinHttpRouter<>(service, path);
	}

	public interface ResourceCaller<SM extends AlexandriaHttpManager<?>> {
		void call(SM manager) throws AlexandriaException;
	}

	private static Javalin create(String webDirectory, long maxResourceSize) {
		Javalin result = Javalin.create(config -> {
			if (isInClasspath(webDirectory)) config.staticFiles.add(webDirectory, Location.CLASSPATH);
			else config.staticFiles.add(webDirectory, Location.EXTERNAL);
			ResourceHandler defaultHandler = config.pvt.resourceHandler;
			config.pvt.resourceHandler = new ResourceHandler() {
				@Override
				public boolean canHandle(Context context) {
					return defaultHandler != null && defaultHandler.canHandle(context);
				}

				@Override
				public boolean handle(Context context) {
					String filePath = context.path();
					InputStream inputStream = AlexandriaHttpServer.class.getClassLoader().getResourceAsStream((webDirectory.endsWith("/") ? webDirectory.substring(0, webDirectory.length()-1) : webDirectory) + filePath);
					if (inputStream != null) {
						context.result(inputStream);
						return true;
					}
					return defaultHandler != null && defaultHandler.handle(context);
				}

				@Override
				public boolean addStaticFileConfig(StaticFileConfig config) {
					return defaultHandler != null && defaultHandler.addStaticFileConfig(config);
				}
			};
		});
		result.exception(Exception.class, (exception, context) -> Logger.error(exception));
		return result;
	}

	private static void addDevEnvironment(StaticFilesConfig staticFiles) {
		try {
			File file = new File(AlexandriaHttpServer.class.getClassLoader().getResource("").toURI().toURL().getFile(), "..");
			if (!file.exists()) return;
			staticFiles.add(file.getAbsolutePath(), Location.EXTERNAL);
		} catch (MalformedURLException | URISyntaxException ignored) {
		}
	}

	private static boolean isInClasspath(String path) {
		if (path == null) return false;
		return AlexandriaHttpServer.class.getClassLoader().getResourceAsStream(path) != null;
	}

}
