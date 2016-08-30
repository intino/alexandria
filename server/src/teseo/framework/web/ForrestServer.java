package teseo.framework.web;

import teseo.framework.actions.AdapterProxy;
import teseo.framework.actions.RequestAdapter;
import teseo.framework.actions.ResponseAdapter;
import teseo.framework.actions.Router;
import teseo.framework.security.SecurityManager;
import teseo.framework.services.PushService;
import teseo.framework.web.actions.DefaultRequestAdapter;
import teseo.framework.web.actions.DefaultResponseAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

public abstract class ForrestServer implements AdapterProxy {

    private final Map<String, RequestAdapter> requestAdapters = new HashMap<>();
    private final Map<String, ResponseAdapter> responseAdapters = new HashMap<>();
    private final String webDirectory;

    public ForrestServer() {
        this("web/");
    }

    public ForrestServer(String webDirectory) {
        this.webDirectory = webDirectory;
    }

    public void register(PushService pushService) {
        router().pushService("/push", pushService);
    }

    public void register(SecurityManager manager) {
        router().securityManager(manager);
    }

    public Router.Routing route(String path) {
        return router().route(path);
    }

    public RegisterRequestAdapterTask adaptRequest(String... methods) {
        return adapter -> register(methods, method -> requestAdapters.put(method, adapter));
    }

    public RegisterResponseAdapterTask adaptResponse(String... methods) {
        return adapter -> register(methods, method -> responseAdapters.put(method, adapter));
    }

    public String webDirectory() {
        return this.webDirectory;
    }

    private void register(String[] methods, Consumer<String> consumer) {
        Stream.of(methods).forEach(consumer::accept);
    }

    protected abstract Router router();

    @Override
    public RequestAdapter requestAdapterOf(String name, Class clazz) {
        return requestAdapters.getOrDefault(name, new DefaultRequestAdapter(clazz));
    }

    @Override
    public ResponseAdapter responseAdapterOf(String name) {
        return responseAdapters.getOrDefault(name, new DefaultResponseAdapter());
    }

    @FunctionalInterface
    public interface RegisterRequestAdapterTask {
        void with(RequestAdapter adapter);
    }

    @FunctionalInterface
    public interface RegisterResponseAdapterTask {
        void with(ResponseAdapter adapter);
    }
}