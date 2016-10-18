package org.siani.pandora.server.spark;

import org.siani.pandora.exceptions.PandoraException;
import org.siani.pandora.server.PandoraSpark;
import org.siani.pandora.server.pushservice.PushService;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.function.Consumer;
import java.util.function.Function;

import static spark.Spark.webSocket;
import static spark.Spark.webSocketIdleTimeoutMillis;

public class SparkRouter<SM extends SparkManager> {
    private Consumer<PushService> pushConsumer = null;
    private Function<SparkManager, Boolean> validator = null;
    protected PushService pushService;

    private static final int OneDay = 24 * 60 * 60 * 1000;

    private final String path;

    public SparkRouter(String path) {
        this.path = path;
    }

    public void before(PandoraSpark.ResourceCaller<SM> caller) {
        Spark.before(path, (rq, rs) -> before(caller, manager(rq, rs)));
    }

    public void get(PandoraSpark.ResourceCaller<SM> caller) {
        Spark.get(path, (rq, rs) -> execute(caller, manager(rq, rs)));
    }

    public void post(PandoraSpark.ResourceCaller<SM> caller) {
        Spark.post(path, (rq, rs) -> execute(caller, manager(rq, rs)));
    }

    public void put(PandoraSpark.ResourceCaller<SM> caller) {
        Spark.put(path, (rq, rs) -> execute(caller, manager(rq, rs)));
    }

    public void delete(PandoraSpark.ResourceCaller<SM> caller) {
        Spark.delete(path, (rq, rs) -> execute(caller, manager(rq, rs)));
    }

    public void after(PandoraSpark.ResourceCaller<SM> caller) {
        Spark.after(path, (rq, rs) -> after(caller, manager(rq, rs)));
    }

    public void push(PushService service) {
        if (this.pushConsumer != null) this.pushConsumer.accept(service);
        PushServiceHandler.inject(service);
        webSocketIdleTimeoutMillis(OneDay);
        webSocket(path, PushServiceHandler.class);
    }

    public void inject(PushService pushService) {
        this.pushService = pushService;
    }

    public void whenRegister(Consumer<PushService> pushServiceConsumer) {
        this.pushConsumer = pushServiceConsumer;
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

    private void before(PandoraSpark.ResourceCaller<SM> caller, SparkManager manager) {
        try {
            caller.call((SM) manager);
        } catch (PandoraException e) {
            e.printStackTrace();
        }
    }

    private Object execute(PandoraSpark.ResourceCaller<SM> caller, SparkManager manager) {
        if (!validRequest(manager)) return "FAILURE";
        try {
            caller.call((SM) manager);
        } catch (PandoraException e) {
            return e.code();
        }
        return "OK";
    }

    private void after(PandoraSpark.ResourceCaller<SM> caller, SparkManager manager) {
        try {
            caller.call((SM) manager);
        } catch (PandoraException e) {
            e.printStackTrace();
        }
    }

}