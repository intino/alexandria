package org.siani.pandora.server;

import org.siani.pandora.exceptions.PandoraException;
import org.siani.pandora.server.pushservice.PushService;
import org.siani.pandora.server.security.NullSecurityManager;
import org.siani.pandora.server.security.PandoraSecurityManager;
import org.siani.pandora.server.spark.SparkManager;
import org.siani.pandora.server.spark.SparkRouter;
import spark.Spark;

import java.util.function.Consumer;
import java.util.function.Function;

public class PandoraSpark<R extends SparkRouter> {
	private PandoraSecurityManager securityManager = new NullSecurityManager();
	private final String webDirectory;
	protected PushService pushService;

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

	public R route(String path) {
		R router = createRouter(path);
		router.inject(pushService);

		router.whenRegister(new Consumer<PushService>() {
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
