package io.intino.alexandria.ui.server;

import io.intino.alexandria.http.AlexandriaHttpServer;
import io.intino.alexandria.http.pushservice.PushService;
import io.intino.alexandria.http.server.AlexandriaHttpManager;
import io.intino.alexandria.http.server.AlexandriaHttpRouter;
import io.intino.alexandria.ui.services.AuthService;

import java.util.function.Consumer;
import java.util.function.Function;

public class UIRouter implements AlexandriaHttpRouter<AlexandriaUiManager> {
	private final AlexandriaHttpRouter<AlexandriaUiManager> router;
	private final AuthService authService;
	private static boolean hasUserHome = false;

	public UIRouter(AlexandriaHttpRouter<AlexandriaUiManager> router, String path, AuthService authService) {
		this.router = router;
		if (isUserHomePath(path)) hasUserHome = true;
		this.authService = authService;
		managerProvider((request, response) -> new AlexandriaUiManager((io.intino.alexandria.ui.services.push.PushService) pushService(), request, response, authService, hasUserHome));
	}

	private boolean isUserHomePath(String path) {
		return path.contains(AlexandriaUiManager.KonosUserHomePath);
	}

	@Override
	public AlexandriaHttpRouter<AlexandriaUiManager> before(AlexandriaHttpServer.ResourceCaller<AlexandriaUiManager> caller) {
		return router.before(caller);
	}

	@Override
	public AlexandriaHttpRouter<AlexandriaUiManager> get(AlexandriaHttpServer.ResourceCaller<AlexandriaUiManager> caller) {
		return router.get(caller);
	}

	@Override
	public AlexandriaHttpRouter<AlexandriaUiManager> post(AlexandriaHttpServer.ResourceCaller<AlexandriaUiManager> caller) {
		return router.post(caller);
	}

	@Override
	public AlexandriaHttpRouter<AlexandriaUiManager> put(AlexandriaHttpServer.ResourceCaller<AlexandriaUiManager> caller) {
		return router.put(caller);
	}

	@Override
	public AlexandriaHttpRouter<AlexandriaUiManager> delete(AlexandriaHttpServer.ResourceCaller<AlexandriaUiManager> caller) {
		return router.delete(caller);
	}

	@Override
	public AlexandriaHttpRouter<AlexandriaUiManager> patch(AlexandriaHttpServer.ResourceCaller<AlexandriaUiManager> caller) {
		return router.patch(caller);
	}

	@Override
	public AlexandriaHttpRouter<AlexandriaUiManager> after(AlexandriaHttpServer.ResourceCaller<AlexandriaUiManager> caller) {
		return router.after(caller);
	}

	@Override
	public PushService<?, ?> pushService() {
		return router.pushService();
	}

	@Override
	public void push(PushService<?, ?> service) {
		router.push(service);
	}

	@Override
	public void register(PushService<?, ?> service) {
		router.register(service);
	}

	@Override
	public void managerProvider(ManagerProvider<AlexandriaUiManager> provider) {
		router.managerProvider(provider);
	}

	@Override
	public void whenRegisterPushService(Consumer<PushService<?, ?>> pushServiceConsumer) {
		router.whenRegisterPushService(pushServiceConsumer);
	}

	@Override
	public void whenValidate(Function<AlexandriaHttpManager<?>, Boolean> validator) {
		router.whenValidate(validator);
	}
}