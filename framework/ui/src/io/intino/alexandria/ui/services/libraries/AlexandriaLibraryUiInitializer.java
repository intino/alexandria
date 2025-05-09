package io.intino.alexandria.ui.services.libraries;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.AlexandriaUiServer;
import io.intino.alexandria.ui.services.push.PushService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AlexandriaLibraryUiInitializer {
	private final AlexandriaLibraryLoader libraryLoader;
	private final Box box;

	public AlexandriaLibraryUiInitializer(Box box, AlexandriaLibraryLoader libraryLoader) {
		super();
		this.box = box;
		this.libraryLoader = libraryLoader;
	}

	public void initialize(AlexandriaUiServer server, PushService pushService) {
		initDisplays(server, pushService);
		initProxyDisplays(server, pushService);
		initExposedDisplays(server, pushService);
	}

	private void initDisplays(AlexandriaUiServer server, PushService pushService) {
		try {
			Class<?> serviceClass = libraryLoader.serviceClass();
			if (serviceClass == null) return;
			Method method = serviceClass.getMethod("initDisplays", AlexandriaUiServer.class, PushService.class);
			method.invoke("initDisplays", server, pushService);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			Logger.error(e);
		}
	}

	private void initProxyDisplays(AlexandriaUiServer server, PushService pushService) {
		try {
			Class<?> serviceClass = libraryLoader.serviceClass();
			if (serviceClass == null) return;
			Method method = serviceClass.getMethod("initProxyDisplays", AlexandriaUiBox.class, AlexandriaUiServer.class, PushService.class);
			method.invoke("initProxyDisplays", box, server, pushService);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			Logger.error(e);
		}
	}

	private void initExposedDisplays(AlexandriaUiServer server, PushService pushService) {
		try {
			Class<?> serviceClass = libraryLoader.serviceClass();
			if (serviceClass == null) return;
			Method method = serviceClass.getMethod("initExposedDisplays", AlexandriaUiBox.class, AlexandriaUiServer.class, PushService.class);
			method.invoke("initExposedDisplays", box, server, pushService);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			Logger.error(e);
		}
	}

}