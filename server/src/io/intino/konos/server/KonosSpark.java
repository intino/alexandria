package io.intino.konos.server;

import io.intino.konos.exceptions.KonosException;
import io.intino.konos.server.pushservice.PushService;
import io.intino.konos.server.security.NullSecurityManager;
import io.intino.konos.server.security.KonosSecurityManager;
import io.intino.konos.server.spark.SparkManager;
import io.intino.konos.server.spark.SparkRouter;
import spark.Spark;

import java.util.function.Consumer;
import java.util.function.Function;

public class KonosSpark<R extends SparkRouter> {
	private KonosSecurityManager securityManager = new NullSecurityManager();
	private final String webDirectory;
	protected PushService pushService;

	protected static final String WebDirectory = "/web";

	public KonosSpark(int port) {
		this(port, WebDirectory);
	}

	public KonosSpark(int port, String webDirectory) {
		Spark.port(port);
		configureStaticFiles(webDirectory);
		this.webDirectory = webDirectory;
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
		return (R) new SparkRouter(path);
	}

	public interface ResourceCaller<SM extends SparkManager> {
		void call(SM manager) throws KonosException;
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
