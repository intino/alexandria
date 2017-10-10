package io.intino.konos.server;

import io.intino.konos.exceptions.KonosException;
import io.intino.konos.server.pushservice.PushService;
import io.intino.konos.server.security.KonosSecurityManager;
import io.intino.konos.server.security.NullSecurityManager;
import io.intino.konos.server.spark.SparkManager;
import io.intino.konos.server.spark.SparkRouter;
import spark.Service;

import java.util.function.Consumer;
import java.util.function.Function;

public class KonosSpark<R extends SparkRouter> {
	private KonosSecurityManager securityManager = new NullSecurityManager();
	protected final String webDirectory;
	protected PushService pushService;
	protected static final String WebDirectory = "/www";
	protected Service service;
	protected int port;

	public KonosSpark(int port) {
		this(port, WebDirectory);
	}

	public KonosSpark(int port, String webDirectory) {
		this.port = port;
		this.webDirectory = webDirectory;
		this.service = Service.ignite();
		configureStaticFiles();
		service.port(this.port);
	}

	public KonosSpark start() {
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

	public void secure(KonosSecurityManager manager) {
		this.securityManager = manager;
	}

	public R route(String path) {
		R router = createRouter(path);
		router.inject(pushService);

		router.whenRegisterPushService(new Consumer<PushService>() {
			@Override
			public void accept(PushService pushService) {
				KonosSpark.this.pushService = pushService;
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
		void call(SM manager) throws KonosException;

	}

	protected void configureStaticFiles() {
		if (isInClasspath(webDirectory)) service.staticFileLocation(webDirectory);
		else service.externalStaticFileLocation(webDirectory);
	}

	private boolean isInClasspath(String path) {
		return getClass().getClassLoader().getResourceAsStream(path) != null;
	}

}
