package io.intino.alexandria.http.server;

import io.intino.alexandria.http.AlexandriaHttpServer.ResourceCaller;
import io.intino.alexandria.http.pushservice.PushService;

import java.util.function.Consumer;
import java.util.function.Function;

public interface AlexandriaHttpRouter<SM extends AlexandriaHttpManager<?>> {
	AlexandriaHttpRouter<SM> before(ResourceCaller<SM> caller);
	AlexandriaHttpRouter<SM> get(ResourceCaller<SM> caller);
	AlexandriaHttpRouter<SM> post(ResourceCaller<SM> caller);
	AlexandriaHttpRouter<SM> put(ResourceCaller<SM> caller);
	AlexandriaHttpRouter<SM> delete(ResourceCaller<SM> caller);
	AlexandriaHttpRouter<SM> patch(ResourceCaller<SM> caller);
	AlexandriaHttpRouter<SM> after(ResourceCaller<SM> caller);
	PushService<?, ?> pushService();
	void push(PushService<?, ?> service);
	void register(PushService<?, ?> service);
	void managerProvider(ManagerProvider<SM> provider);
	void whenRegisterPushService(Consumer<PushService<?, ?>> pushServiceConsumer);
	void whenValidate(Function<AlexandriaHttpManager<?>, Boolean> validator);

	interface ManagerProvider<SM extends AlexandriaHttpManager<?>> {
		SM get(AlexandriaHttpRequest request, AlexandriaHttpResponse response);
	}
}