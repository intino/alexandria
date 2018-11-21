package io.intino.alexandria.assa;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

public class AssaWriter {
	private final File file;
	private final Map<Object, Integer> objects;

	public AssaWriter(File file) {
		this.file = file;
		this.objects = new HashMap<>();
	}

	public void save(String name, AssaStream input) throws IOException {
		try (DataOutputStream output = output()) {
			output.writeUTF(name);
			output.writeInt(input.size());
			writeItems(input, output);
			output.writeInt(objects.size());
			writeObjects(objectOutputStream(output));
		}
	}

	private DataOutputStream output() throws FileNotFoundException {
		return new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
	}

	private void writeItems(AssaStream input, DataOutputStream output) throws IOException {
		while (input.hasNext()) {
			AssaStream.Item item = input.next();
			if (item == null) break;
			output.writeLong(item.key());
			output.writeInt(map(item.object()));
		}
	}

	private void writeObjects(ObjectOutputStream output) {
		objects().forEach(o -> writeObject(output, o));
	}

	private void writeObject(ObjectOutputStream output, Object object) {
		try {
			output.writeObject(object);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private int map(Object object) {
		if (objects.containsKey(object)) return objects.get(object);
		objects.put(object, objects.size());
		return objects.size() - 1;
	}

	private Stream<Object> objects() {
		return objects.entrySet()
				.stream()
				.sorted(comparing(Map.Entry::getValue))
				.map(Map.Entry::getKey);
	}

	private ObjectOutputStream objectOutputStream(DataOutputStream output) throws IOException {
		return new ObjectOutputStream(output);
	}


}
