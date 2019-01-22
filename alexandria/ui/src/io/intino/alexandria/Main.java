package io.intino.alexandria;

import io.intino.alexandria.core.Box;

public class Main {
	public static void main(String[] args) {
		Box box = new UiFrameworkBox(args);
		Runtime.getRuntime().addShutdownHook(new Thread(box::close));
	}
}