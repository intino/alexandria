package io.intino.alexandria.led;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class LedWriter {
	private final OutputStream outputStream;

	public LedWriter(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public void write(PrimaryLed<? extends Schema> led) {
		Kryo kryo = new Kryo();
		Class<?> itemClass = led.iterator().next().getClass();
		List<FieldMask> masks = Arrays.stream(itemClass.getDeclaredFields()).map(this::maskOf).filter(Objects::nonNull).collect(toList());
		Integer itemSize = masks.stream().map(m -> m.size).reduce(0, Integer::sum);
		kryo.register(itemClass);
		try (Output output = new Output(new BufferedOutputStream(outputStream))) {
			output.writeInt(led.size());
			output.writeInt(itemSize);
			masks.forEach(value -> output.writeShort(value.ordinal()));
			output.writeShort(-1);
			led.iterator().forEachRemaining(i -> kryo.writeObject(output, i));
		}
	}

	private FieldMask maskOf(Field f) {
		String simpleName = f.getType().getSimpleName().toLowerCase();
		return Arrays.stream(FieldMask.values()).filter(v -> simpleName.contains(v.name().toLowerCase())).findFirst().orElse(null);
	}
}