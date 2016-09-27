package org.siani.pandora.server.actions;

import org.siani.pandora.server.security.TeseoSecurityManager;
import org.siani.pandora.server.security.NullSecurityManager;
import org.siani.pandora.server.services.PushService;

public abstract class Router {
    private TeseoSecurityManager teseoSecurityManager = new NullSecurityManager();

    public Routing route(String path) {
        return new Routing(path);
    }

    protected abstract Router register(Routing routing);

    public abstract Router staticFiles(String path);

    public abstract void pushService(String route, PushService service);

    public void securityManager(TeseoSecurityManager manager) {
        this.teseoSecurityManager = manager;
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

    protected TeseoSecurityManager securityManager() {
        return this.teseoSecurityManager;
    }
}
