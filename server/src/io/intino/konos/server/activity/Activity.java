package io.intino.konos.server.activity;

import io.intino.konos.server.activity.displays.Display;
import io.intino.konos.server.activity.displays.DisplayNotifier;
import io.intino.konos.server.activity.displays.DisplayNotifierProvider;
import io.intino.konos.server.activity.displays.MessageCarrier;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class Activity {
    private static Map<Class<? extends Display>, Class<? extends DisplayNotifier>> notifiers = new HashMap<>();

    protected static <DN extends DisplayNotifier> DisplayNotifierRegistration register(Class<DN> notifierClass) {
        return new DisplayNotifierRegistration() {
            @Override
            public <D extends Display> void forDisplay(Class<D> displayClass) {
                notifiers.put(displayClass, notifierClass);
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
        Class<? extends DisplayNotifier> clazz = notifiers.get(display.getClass());;

        if (clazz == null)
			clazz = notifiers.getOrDefault(displayByInheritance(display.getClass()), DisplayNotifier.class);

        return clazz.getConstructor(Display.class, MessageCarrier.class);
    }

    private static Class<? extends Display> displayByInheritance(Class<? extends Display> clazz) {
        return notifiers.keySet().stream().filter(dc -> dc.isAssignableFrom(clazz)).findFirst().orElse(null);
    }

    protected interface DisplayNotifierRegistration {
        <D extends Display> void forDisplay(Class<D> displayClass);
    }

}
