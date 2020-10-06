package io.intino.alexandria.led;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LedReader {
	private final InputStream inputStream;

	public LedReader(File file) {
		InputStream s;
		try {
			s = new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			s = InputStream.nullInputStream();
		}
		this.inputStream = s;
	}

	public LedReader(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public <S extends Schema> PrimaryLed<S> read(Class<S> clazz) {
		Kryo kryo = new Kryo();
		kryo.register(clazz);
		List<S> items = new ArrayList<>();
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

	public int size() {
		int size;
		try (Input input = new Input(new BufferedInputStream(inputStream))) {
			size = input.readInt();
		}
		return size;
	}
}
