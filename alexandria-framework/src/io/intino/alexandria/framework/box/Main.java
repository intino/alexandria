package io.intino.alexandria.framework.box;

import io.intino.alexandria.Box;

public class Main {
	public static void main(String[] args) {
		Box box = new AlexandriaFrameworkBox(args).open();
		Runtime.getRuntime().addShutdownHook(new Thread(box::close));
	}
}