package io.intino.alexandria.ui;

import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.DisplayRouteDispatcher;
import io.intino.alexandria.ui.displays.DisplayRouteManager;
import io.intino.alexandria.ui.displays.notifiers.DisplayNotifier;
import io.intino.alexandria.ui.displays.notifiers.DisplayNotifierProvider;
import io.intino.alexandria.ui.spark.UISparkManager;
import io.intino.alexandria.wss.pushservice.MessageCarrier;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;

public abstract class UI {
    private static Map<Class<? extends Display>, Class<? extends DisplayNotifier>> notifiers = new HashMap<>();

    protected static <DN extends DisplayNotifier> DisplayNotifierRegistration register(Class<DN> notifierClass) {
        return new DisplayNotifierRegistration() {
            @Override
            public <D extends Display> void forDisplay(Class<D> displayClass) {
                notifiers.put(displayClass, notifierClass);
            }
        };
    }

    protected static DisplayRouteManager routeManager(UISpark spark, DisplayRouteDispatcher routeDispatcher) {
        return new DisplayRouteManager() {
            @Override
            public void get(String path, Consumer<UISparkManager> consumer) {
                spark.route(path).get(consumer::accept);
            }

            @Override
            public void post(String path, Consumer<UISparkManager> consumer) {
                spark.route(path).post(consumer::accept);
            }

            @Override
            public DisplayRouteDispatcher routeDispatcher() {
                return routeDispatcher;
            }
        };
    }

    protected static DisplayNotifierProvider notifierProvider() {
        return (display, carrier) -> {
            try {
                return notifierFor(display).newInstance(display, carrier);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException error) {
                System.err.println(error.getMessage());
                return null;
            }
        };
    }

    private static Constructor<? extends DisplayNotifier> notifierFor(Display display) throws NoSuchMethodException {
        Class<? extends DisplayNotifier> clazz = notifiers.get(display.getClass());

        if (clazz == null)
			clazz = notifiers.getOrDefault(displayByInheritance(display.getClass()), DisplayNotifier.class);

        return clazz.getConstructor(Display.class, MessageCarrier.class);
    }

    private static Class<? extends Display> displayByInheritance(Class<? extends Display> clazz) {
        Class<?> superclass = clazz.getSuperclass();
        if (Display.class.isAssignableFrom(superclass) && notifiers.containsKey(superclass)) return (Class<? extends Display>) superclass;
        List<Class<? extends Display>> result = notifiers.keySet().stream().filter(dc -> dc.isAssignableFrom(clazz)).collect(toList());
        return result.size() > 0 ? result.get(result.size()-1) : null;
    }

    protected interface DisplayNotifierRegistration {
        <D extends Display> void forDisplay(Class<D> displayClass);
    }

}
