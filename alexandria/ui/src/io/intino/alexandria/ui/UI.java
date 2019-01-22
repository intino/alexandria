package io.intino.alexandria.ui;

import io.intino.alexandria.rest.pushservice.MessageCarrier;
import io.intino.alexandria.ui.displays.AlexandriaDisplay;
import io.intino.alexandria.ui.displays.notifiers.AlexandriaDisplayNotifier;
import io.intino.alexandria.ui.displays.notifiers.AlexandriaDisplayNotifierProvider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class UI {
    private static Map<Class<? extends AlexandriaDisplay>, Class<? extends AlexandriaDisplayNotifier>> notifiers = new HashMap<>();

    protected static <DN extends AlexandriaDisplayNotifier> DisplayNotifierRegistration register(Class<DN> notifierClass) {
        return new DisplayNotifierRegistration() {
            @Override
            public <D extends AlexandriaDisplay> void forDisplay(Class<D> displayClass) {
                notifiers.put(displayClass, notifierClass);
            }
        };
    }

    protected static AlexandriaDisplayNotifierProvider notifierProvider() {
        return (display, carrier) -> {
            try {
                return notifierFor(display).newInstance(display, carrier);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException error) {
                System.err.println(error.getMessage());
                return null;
            }
        };
    }

    private static Constructor<? extends AlexandriaDisplayNotifier> notifierFor(AlexandriaDisplay display) throws NoSuchMethodException {
        Class<? extends AlexandriaDisplayNotifier> clazz = notifiers.get(display.getClass());;

        if (clazz == null)
			clazz = notifiers.getOrDefault(displayByInheritance(display.getClass()), AlexandriaDisplayNotifier.class);

        return clazz.getConstructor(AlexandriaDisplay.class, MessageCarrier.class);
    }

    private static Class<? extends AlexandriaDisplay> displayByInheritance(Class<? extends AlexandriaDisplay> clazz) {
        return notifiers.keySet().stream().filter(dc -> dc.isAssignableFrom(clazz)).findFirst().orElse(null);
    }

    protected interface DisplayNotifierRegistration {
        <D extends AlexandriaDisplay> void forDisplay(Class<D> displayClass);
    }

}
