package io.intino.alexandria;

import io.intino.alexandria.core.Box;

public class Main {
	public static void main(String[] args) {
		Box box = new UiFrameworkBox(args);
		box.start();
		Runtime.getRuntime().addShutdownHook(new Thread(box::stop));
	}
}