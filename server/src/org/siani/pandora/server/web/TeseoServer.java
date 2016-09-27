package org.siani.pandora.server.web;

import org.siani.pandora.server.actions.RequestAdapter;
import org.siani.pandora.server.actions.ResponseAdapter;
import org.siani.pandora.server.actions.Router;
import org.siani.pandora.server.security.TeseoSecurityManager;
import org.siani.pandora.server.services.PushService;
import org.siani.pandora.server.web.actions.DefaultRequestAdapter;
import org.siani.pandora.server.web.actions.DefaultResponseAdapter;
import org.siani.pandora.server.actions.AdapterProxy;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

public abstract class TeseoServer implements AdapterProxy {

    private final Map<String, RequestAdapter> requestAdapters = new HashMap<>();
    private final Map<String, ResponseAdapter> responseAdapters = new HashMap<>();
    private final String webDirectory;

    public TeseoServer() {
        this("web/");
    }

    public TeseoServer(String webDirectory) {
        this.webDirectory = webDirectory;
    }

    public void register(PushService pushService) {
        router().pushService("/push", pushService);
    }

    public void register(TeseoSecurityManager manager) {
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