package io.intino.alexandria;

import io.intino.alexandria.core.Box;
import org.apache.log4j.Level;

public class Main {
	public static void main(String[] args) {
		io.intino.alexandria.logger4j.Logger.init(Level.ERROR);
		Box box = new UiFrameworkBox(args);
		box.start();
		Runtime.getRuntime().addShutdownHook(new Thread(box::stop));
	}
}