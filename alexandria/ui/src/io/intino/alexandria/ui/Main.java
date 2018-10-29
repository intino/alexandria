package io.intino.alexandria.ui;

import io.intino.konos.framework.Box;

public class Main {
	public static void main(String[] args) {
		Box box = new AlexandriaFrameworkBox(args).open();
		Runtime.getRuntime().addShutdownHook(new Thread(box::close));
	}
}