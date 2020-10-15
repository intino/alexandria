package io.intino.alexandria.led.io;

import io.intino.alexandria.led.Led;
import io.intino.alexandria.led.Schema;

public interface LedWriter {
	void write(Led<? extends Schema> led);
}
