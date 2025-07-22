package io.intino.alexandria.ui.services.libraries;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.AlexandriaUiServer;
import io.intino.alexandria.ui.services.push.PushService;
import io.intino.alexandria.ui.services.translator.Dictionary;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class AlexandriaLibraryUiInitializer {
	private final AlexandriaLibraryLoader libraryLoader;
	private final AlexandriaUiBox box;

	public AlexandriaLibraryUiInitializer(AlexandriaUiBox box, AlexandriaLibraryLoader libraryLoader) {
		super();
		this.box = box;
		this.libraryLoader = libraryLoader;
	}

	public void initialize(AlexandriaUiServer server, PushService pushService) {
		initTranslatorService(server, pushService);
		initDisplays(server, pushService);
		initProxyDisplays(server, pushService);
		initExposedDisplays(server, pushService);
	}

	private void initTranslatorService(AlexandriaUiServer server, PushService pushService) {
		try {
			Class<?> i18nClass = libraryLoader.i18nClass();
			if (i18nClass == null) return;
			Method method = i18nClass.getMethod("dictionaries");
			Object dictionaries = method.invoke(null);
			box.translatorService().addAll((List< Dictionary>) dictionaries);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			Logger.error(e);
		}
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