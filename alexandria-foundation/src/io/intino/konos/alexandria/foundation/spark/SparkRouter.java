package io.intino.konos.alexandria.foundation.spark;

import io.intino.konos.alexandria.exceptions.AlexandriaException;
import io.intino.konos.alexandria.foundation.AlexandriaSpark;
import spark.Request;
import spark.Response;
import spark.Service;

import java.util.function.Consumer;
import java.util.function.Function;

public class SparkRouter<SM extends SparkManager> {
	private Function<SparkManager, Boolean> validator = null;
	private Consumer<PushService> pushServiceConsumer = null;
	protected PushService pushService;

	private static final int OneDay = 24 * 60 * 60 * 1000;

	private Service service;
	private final String path;

	public SparkRouter(Service service, String path) {
		this.service = service;
		this.path = path;
	}

	public void before(AlexandriaSpark.ResourceCaller<SM> caller) {
		service.before(path, (rq, rs) -> before(caller, manager(rq, rs)));
	}

	public void get(AlexandriaSpark.ResourceCaller<SM> caller) {
		service.get(path, (rq, rs) -> execute(caller, manager(rq, rs)));
	}

	public void post(AlexandriaSpark.ResourceCaller<SM> caller) {
		service.post(path, (rq, rs) -> execute(caller, manager(rq, rs)));
	}

	public void put(AlexandriaSpark.ResourceCaller<SM> caller) {
		service.put(path, (rq, rs) -> execute(caller, manager(rq, rs)));
	}

	public void delete(AlexandriaSpark.ResourceCaller<SM> caller) {
		service.delete(path, (rq, rs) -> execute(caller, manager(rq, rs)));
	}

	public void after(AlexandriaSpark.ResourceCaller<SM> caller) {
		service.after(path, (rq, rs) -> after(caller, manager(rq, rs)));
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
		return (SM) new SparkManager(rq, rs);
	}

	private boolean validRequest(SparkManager manager) {
		return validator != null ? validator.apply(manager) : true;
	}

	private void before(AlexandriaSpark.ResourceCaller<SM> caller, SparkManager manager) {
		try {
			caller.call((SM) manager);
		} catch (AlexandriaException e) {
			manager.response.status(Integer.parseInt(e.code()));
			e.printStackTrace();
		}
	}

	private Object execute(AlexandriaSpark.ResourceCaller<SM> caller, SparkManager manager) {
		if (!validRequest(manager)) return "FAILURE";
		try {
			caller.call((SM) manager);
		} catch (AlexandriaException e) {
			manager.response.status(Integer.parseInt(e.code()));
			return e.code();
		}
		return "OK";
	}

	private void after(AlexandriaSpark.ResourceCaller<SM> caller, SparkManager manager) {
		try {
			caller.call((SM) manager);
		} catch (AlexandriaException e) {
			manager.response.status(Integer.parseInt(e.code()));
			e.printStackTrace();
		}
	}

}