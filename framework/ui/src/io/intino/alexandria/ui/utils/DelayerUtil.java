package io.intino.alexandria.ui.utils;

import io.intino.alexandria.ui.displays.Display;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class DelayerUtil {
	private static Map<Display, Timer> timers = new HashMap<>();

	public static void execute(Display display, Consumer<Void> callback, int delay) {
		if (timers.containsKey(display)) timers.get(display).cancel();
		timers.put(display, new Timer("delayer util timer"));
		timers.get(display).schedule(new TimerTask() {
			@Override
			public void run() {
				callback.accept(null);
				timers.remove(display);
			}
		}, 1000);
	}

}
