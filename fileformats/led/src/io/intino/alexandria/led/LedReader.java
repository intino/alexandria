package io.intino.alexandria.led;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LedReader {

	private final InputStream inputStream;

	public LedReader(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public <R extends Item> Led<R> read(Class<R> clazz) {
		Kryo kryo = new Kryo();
		kryo.register(clazz);
		List<R> items = new ArrayList<>();
		try (Input input = new Input(new BufferedInputStream(inputStream))) {
			input.readInt();
			input.readInt();
			short read = 0;
			while (read != -1)
				read = input.readShort();
			while (!input.eof()) items.add(kryo.readObject(input, clazz));
		}
		return new PrimaryLed<>(items);
	}
}
