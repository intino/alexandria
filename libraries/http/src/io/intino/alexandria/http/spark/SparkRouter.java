package io.intino.alexandria.http.spark;

import io.intino.alexandria.exceptions.AlexandriaException;
import io.intino.alexandria.http.AlexandriaSpark;
import io.intino.alexandria.logger.Logger;
import spark.Request;
import spark.Response;
import spark.Service;

import java.util.function.Consumer;
import java.util.function.Function;

public class SparkRouter<SM extends SparkManager> {
	private static final int OneDay = 24 * 60 * 60 * 1000;
	private final String path;
	protected PushService pushService;
	private Function<SparkManager, Boolean> validator = null;
	private Consumer<PushService> pushServiceConsumer = null;
	private Service service;

	public SparkRouter(Service service, String path) {
		this.service = service;
		this.path = path;
	}

	public SparkRouter<SM> before(AlexandriaSpark.ResourceCaller<SM> caller) {
		service.before(path, (rq, rs) -> before(caller, manager(rq, rs)));
		return this;
	}

	public SparkRouter<SM> get(AlexandriaSpark.ResourceCaller<SM> caller) {
		service.get(path, (rq, rs) -> SparkRouter.this.execute(caller, SparkRouter.this.manager(rq, rs)));
		return this;
	}

	public SparkRouter<SM> post(AlexandriaSpark.ResourceCaller<SM> caller) {
		service.post(path, (rq, rs) -> execute(caller, manager(rq, rs)));
		return this;
	}

	public SparkRouter<SM> put(AlexandriaSpark.ResourceCaller<SM> caller) {
		service.put(path, (rq, rs) -> execute(caller, manager(rq, rs)));
		return this;
	}

	public SparkRouter<SM> delete(AlexandriaSpark.ResourceCaller<SM> caller) {
		service.delete(path, (rq, rs) -> execute(caller, manager(rq, rs)));
		return this;
	}

	public SparkRouter<SM> patch(AlexandriaSpark.ResourceCaller<SM> caller) {
		service.patch(path, (rq, rs) -> execute(caller, manager(rq, rs)));
		return this;
	}

	public SparkRouter<SM> after(AlexandriaSpark.ResourceCaller<SM> caller) {
		service.after(path, (rq, rs) -> after(caller, manager(rq, rs)));
		return this;
	}


	public void push(PushService service) {
		if (this.pushService != null) return;
		if (this.pushServiceConsumer != null) this.pushServiceConsumer.accept(service);
		PushServiceHandler.inject(service);
		this.service.webSocketIdleTimeoutMillis(OneDay);
		this.service.webSocket(path, PushServiceHandler.class);
	}

	public void inject(PushService pushService) {
		this.pushService = pushService;
	}

	public void whenRegisterPushService(Consumer<PushService> pushServiceConsumer) {
		this.pushServiceConsumer = pushServiceConsumer;
	}

	public void whenValidate(Function<SparkManager, Boolean> validator) {
		this.validator = validator;
	}

	protected SM manager(Request rq, Response rs) {
		return (SM) new SparkManager(pushService, rq, rs);
	}

	private boolean validRequest(SparkManager manager) {
		return validator != null ? validator.apply(manager) : true;
	}

	private String before(AlexandriaSpark.ResourceCaller<SM> caller, SparkManager manager) {
		try {
			caller.call((SM) manager);
		} catch (AlexandriaException e) {
			manager.response.status();
			manager.response.body(e.toString());
			service.halt(Integer.parseInt(e.code()), e.toString());
		}
		return null;
	}

	private Object execute(AlexandriaSpark.ResourceCaller<SM> caller, SparkManager manager) {
		if (!validRequest(manager)) return "FAILURE";
		return call(caller, manager);
	}

	private Object after(AlexandriaSpark.ResourceCaller<SM> caller, SparkManager manager) {
		return call(caller, manager);
	}

	private Object call(AlexandriaSpark.ResourceCaller<SM> caller, SparkManager manager) {
		try {
			caller.call((SM) manager);
		} catch (AlexandriaException e) {
			manager.response.status(Integer.parseInt(e.code()));
			manager.response.body(e.toString());
			service.halt(Integer.parseInt(e.code()), e.toString());
		} catch (Throwable e) {
			Logger.error(e);
			manager.response.status(500);
			service.halt(500);
		}
		return "OK";
	}
}