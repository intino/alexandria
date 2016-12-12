package io.intino.pandora.server;

import io.intino.pandora.exceptions.PandoraException;
import io.intino.pandora.server.pushservice.PushService;
import io.intino.pandora.server.security.NullSecurityManager;
import io.intino.pandora.server.security.PandoraSecurityManager;
import io.intino.pandora.server.spark.SparkManager;
import io.intino.pandora.server.spark.SparkRouter;
import spark.Spark;

import java.util.function.Consumer;
import java.util.function.Function;

public class PandoraSpark<R extends SparkRouter> {
	private PandoraSecurityManager securityManager = new NullSecurityManager();
	private final String webDirectory;
	protected PushService pushService;

	protected static final String WebDirectory = "/web";

	public PandoraSpark(int port) {
		this(port, WebDirectory);
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

	public R route(String path) {
		R router = createRouter(path);
		router.inject(pushService);

		router.whenRegisterPushService(new Consumer<PushService>() {
			@Override
			public void accept(PushService pushService) {
				PandoraSpark.this.pushService = pushService;
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
		void call(SM manager) throws PandoraException;
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
