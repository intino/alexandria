package teseo.framework.actions;

import teseo.framework.security.NullSecurityManager;
import teseo.framework.security.SecurityManager;
import teseo.framework.services.PushService;

public abstract class Router {
    private SecurityManager securityManager = new NullSecurityManager();

    public Routing route(String path) {
        return new Routing(path);
    }

    protected abstract Router register(Routing routing);

    public abstract Router staticFiles(String path);

    public abstract void pushService(String route, PushService service);

    public void securityManager(SecurityManager manager) {
        this.securityManager = manager;
    }

    public class Routing {
        private final String path;
        private Method method = Method.Get;
        private Action action;

        public Routing(String path) {
            this.path = path;
        }

        public Routing as(Method method) {
            this.method = method;
            return this;
        }

        public void with(Action action) {
            this.action = action;
            register(this);
        }

        public String path() {
            return path;
        }

        public Method method() {
            return method;
        }

        public Action action() {
            return action;
        }
    }

    public enum Method {
        Get, Post, Put, Delete
    }

    protected SecurityManager securityManager() {
        return this.securityManager;
    }
}
