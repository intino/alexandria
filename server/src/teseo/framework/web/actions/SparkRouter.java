package teseo.framework.web.actions;

import teseo.framework.actions.Action;
import teseo.framework.actions.AdapterProxy;
import teseo.framework.actions.Router;
import teseo.framework.security.Secure;
import teseo.framework.services.PushService;
import teseo.framework.web.services.PushServiceHandler;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static spark.Spark.*;

public class SparkRouter extends Router {
	private final Map<Method, RouteBuilder> methods = new HashMap<>();
	private final AdapterProxy adapters;

	private static final String ParamHash = "hash";
	private static final String ParamSignature = "signature";
	private static final int OneDay = 24 * 60 * 60 * 1000;

	public SparkRouter(int port, AdapterProxy adapters) {
		this.adapters = adapters;
		port(port);
		registerMethods();
	}

	@Override
	public void pushService(String route, PushService service) {
		PushServiceHandler.inject(service);
		webSocketIdleTimeoutMillis(OneDay);
		webSocket(route, PushServiceHandler.class);
	}

	private void registerMethods() {
		methods.put(Method.Get, (path, action) -> get(path, (req, res) -> execute(action, req, res)));
		methods.put(Method.Post, (path, action) -> post(path, (req, res) -> execute(action, req, res)));
		methods.put(Method.Put, (path, action) -> put(path, (req, res) -> execute(action, req, res)));
		methods.put(Method.Delete, (path, action) -> delete(path, (req, res) -> execute(action, req, res)));
	}

	@SuppressWarnings("unchecked")
	private String execute(Action action, Request request, Response response) {
		if (!validRequest(action, request))
			return "FAILURE";

		action.task().execute(input(action, request), output(action, response));
		return "OK";
	}

	private boolean validRequest(Action action, Request request) {
		if (!(action instanceof Secure))
			return true;

		String hash = request.queryParams(ParamHash);
		String signature = request.queryParams(ParamSignature);

		return securityManager().check(hash, signature);
	}

	private Action.Input input(Action action, Request request) {
		return SparkWrapper.wrap(request).with(adapters).as(inputClassOf(action));
	}

	private Action.Output output(Action action, Response response) {
		return SparkWrapper.wrap(response).with(adapters).as(outputClassOf(action));
	}

	@SuppressWarnings("unchecked")
	private Class<Action.Input> inputClassOf(Action action) {
		return (Class<Action.Input>) messageClass(forInput(of(action)));
	}

	@SuppressWarnings("unchecked")
	private Class<Action.Output> outputClassOf(Action action) {
		return (Class<Action.Output>) messageClass(forOutput(of(action)));
	}

	private Class<?> messageClass(String type) {
		try {
			return Class.forName(type);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	private String forInput(String text) {
		String inputClass = parse(text, '<', ',');
		return inputClass != null ? inputClass : Action.Input.class.getName();
	}

	private String forOutput(String text) {
		String outputClass = parse(text, ' ', '>');
		return outputClass != null ? outputClass : Action.Output.class.getName();
	}

	private String of(Action action) {
		return taskMethodOf(action).getGenericReturnType().getTypeName();
	}

	private java.lang.reflect.Method taskMethodOf(Action action) {
		List<java.lang.reflect.Method> methods = asList(action.getClass().getMethods()).stream().filter(m -> m.getName().equals("task")).collect(toList());

		if (methods.size() == 1)
			return methods.get(0);

		return methods.stream().filter(m -> !m.getReturnType().getName().equals("Action$Task")).findFirst().get();
	}

	private String parse(String text, char from, char to) {
		if (text.indexOf(from) == -1) return null;
		return text.substring(text.indexOf(from) + 1, text.indexOf(to));
	}

	@Override
	protected Router register(Routing routing) {
		registerBeforeAndAfter(routing.path(), routing.action());
		with(routing.method()).register(routing.path(), routing.action());
		return this;
	}

	@SuppressWarnings("unchecked")
	private void registerBeforeAndAfter(String path, Action action) {
		before(path, (request, response) -> action.task().before(input(action, request), output(action, response)));
		after(path, (request, response) -> action.task().after(input(action, request), output(action, response)));
	}

	private RouteBuilder with(Method method) {
		return methods.get(method);
	}

	@Override
	public Router staticFiles(String path) {
		if (isInClasspath(path))
			staticFileLocation(path);
		else
			externalStaticFileLocation(path);
		return this;
	}

	private boolean isInClasspath(String path) {
		return getClass().getClassLoader().getResourceAsStream(path) != null;
	}

	@FunctionalInterface
	private interface RouteBuilder {
		void register(String path, Action action);
	}
}
