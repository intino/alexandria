package io.intino.konos.alexandria.ui;

import io.intino.konos.alexandria.Box;

public class Main {
	public static void main(String[] args) {
		Box box = new AlexandriaFrameworkBox(args).open();
		Runtime.getRuntime().addShutdownHook(new Thread(box::close));
	}
}