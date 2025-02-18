package io.intino.alexandria.http.javalin;

import io.intino.alexandria.Resource;
import io.intino.alexandria.exceptions.AlexandriaException;
import io.intino.alexandria.http.AlexandriaHttpServer.ResourceCaller;
import io.intino.alexandria.http.pushservice.PushService;
import io.intino.alexandria.http.pushservice.PushServiceHandler;
import io.intino.alexandria.http.server.*;
import io.intino.alexandria.logger.Logger;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JavalinHttpRouter<SM extends AlexandriaHttpManager<?>> implements AlexandriaHttpRouter<SM> {
	private final String path;
	private final Javalin service;
	private ManagerProvider<SM> managerProvider;
	protected PushService<?, ?> pushService;
	private Function<AlexandriaHttpManager<?>, Boolean> validator = null;
	private Consumer<PushService<?, ?>> pushServiceConsumer = null;
	private static final Set<String> Paths = new HashSet<>();

	public JavalinHttpRouter(Javalin service, String path) {
		this.service = service;
		this.path = path;
		this.managerProvider = this::defaultManager;
	}

	@Override
	public JavalinHttpRouter<SM> before(ResourceCaller<SM> caller) {
		if (!canRegister(path, "before")) return this;
		service.before(adapt(path), (context) -> before(caller, manager(context)));
		return this;
	}

	@Override
	public JavalinHttpRouter<SM> get(ResourceCaller<SM> caller) {
		if (!canRegister(path, "get")) return this;
		service.get(adapt(path), (context) -> JavalinHttpRouter.this.execute(caller, manager(context)));
		return this;
	}

	@Override
	public JavalinHttpRouter<SM> post(ResourceCaller<SM> caller) {
		if (!canRegister(path, "post")) return this;
		service.post(adapt(path), (context) -> execute(caller, manager(context)));
		return this;
	}

	@Override
	public JavalinHttpRouter<SM> put(ResourceCaller<SM> caller) {
		if (!canRegister(path, "put")) return this;
		service.put(adapt(path), (context) -> execute(caller, manager(context)));
		return this;
	}

	@Override
	public JavalinHttpRouter<SM> delete(ResourceCaller<SM> caller) {
		if (!canRegister(path, "delete")) return this;
		service.delete(adapt(path), (context) -> execute(caller, manager(context)));
		return this;
	}

	@Override
	public JavalinHttpRouter<SM> patch(ResourceCaller<SM> caller) {
		if (!canRegister(path, "patch")) return this;
		service.patch(adapt(path), (context) -> execute(caller, manager(context)));
		return this;
	}

	@Override
	public JavalinHttpRouter<SM> after(ResourceCaller<SM> caller) {
		if (!canRegister(path, "after")) return this;
		service.after(adapt(path), (context) -> after(caller, manager(context)));
		return this;
	}

	@Override
	public PushService<?, ?> pushService() {
		return pushService;
	}

	@Override
	public void push(PushService<?, ?> service) {
		this.pushService = service;
	}

	@Override
	public void register(PushService<?, ?> service) {
		this.pushService = service;
		if (this.pushServiceConsumer != null) this.pushServiceConsumer.accept(pushService);
		if (!canRegister(path, "socket")) return;
		this.service.ws(adapt(path), config -> new PushServiceHandler(config, pushService));
	}

	@Override
	public void managerProvider(ManagerProvider<SM> provider) {
		this.managerProvider = provider;
	}

	@Override
	public void whenRegisterPushService(Consumer<PushService<?, ?>> pushServiceConsumer) {
		this.pushServiceConsumer = pushServiceConsumer;
	}

	@Override
	public void whenValidate(Function<AlexandriaHttpManager<?>, Boolean> validator) {
		this.validator = validator;
	}

	private SM defaultManager(AlexandriaHttpRequest request, AlexandriaHttpResponse response, AlexandriaHttpResourceProvider resourceProvider) {
		return (SM) new AlexandriaHttpManager<>(pushService, request, response, resourceProvider);
	}

	private SM manager(Context context) {
		return managerProvider.get(request(context), response(context), resourceProvider(context));
	}

	private boolean validRequest(AlexandriaHttpManager<?> manager) {
		return validator != null ? validator.apply(manager) : true;
	}

	private void before(ResourceCaller<SM> caller, AlexandriaHttpManager<?> manager) {
		try {
			caller.call((SM) manager);
		} catch (AlexandriaException e) {
			manager.response().status(Integer.parseInt(e.code()));
			manager.body(e.toString());
			throw new RuntimeException("Error " + Integer.parseInt(e.code()) + " processing request: " + e);
		}
	}

	private void execute(ResourceCaller<SM> caller, AlexandriaHttpManager manager) {
		if (!validRequest(manager)) return;
		call(caller, manager);
	}

	private void after(ResourceCaller<SM> caller, AlexandriaHttpManager manager) {
		call(caller, manager);
	}

	private void call(ResourceCaller<SM> caller, AlexandriaHttpManager manager) {
		try {
			caller.call((SM) manager);
		} catch (AlexandriaException e) {
			manager.response().status(Integer.parseInt(e.code()));
			manager.body(e.toString());
			throw new RuntimeException("Error " + Integer.parseInt(e.code()) + " processing request: " + e);
		} catch (Throwable e) {
			Logger.error(e);
			manager.response().status(500);
			throw new RuntimeException("Error 500 processing request");
		}
	}

	private AlexandriaHttpRequest request(Context context) {
		return new JavalinHttpRequest(context);
	}

	private AlexandriaHttpResponse response(Context context) {
		return new JavalinHttpResponse(context);
	}

	private AlexandriaHttpResourceProvider resourceProvider(Context context) {
		return new AlexandriaHttpResourceProvider() {
			@Override
			public List<Resource> resources() {
				return context.uploadedFiles().stream().map(f -> new Resource(f.filename(), f.content())).collect(Collectors.toList());
			}

			@Override
			public Resource resource(String name) {
				UploadedFile file = context.uploadedFile(name);
				return file != null ? new Resource(file.filename(), file.content()) : null;
			}
		};
	}

	private static String adapt(String path) {
		String regex = ":(\\w+)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(path);
		return matcher.replaceAll("{$1}");
	}

	private boolean canRegister(String path, String suffix) {
		if (Paths.contains(path + suffix)) return false;
		Paths.add(path + suffix);
		return true;
	}

}