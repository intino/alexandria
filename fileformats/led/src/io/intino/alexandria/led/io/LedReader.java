package io.intino.alexandria.led.io;


import io.intino.alexandria.led.Led;
import io.intino.alexandria.led.Schema;
import io.intino.alexandria.led.allocators.SchemaFactory;

public interface LedReader {
	<S extends Schema> Led<S> read(SchemaFactory<S> provider);
}
