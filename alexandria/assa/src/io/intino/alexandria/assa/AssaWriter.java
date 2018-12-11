package io.intino.alexandria.assa;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class AssaWriter<T extends Serializable> {
	private final File file;
//	private final Map<Object, Integer> objects;

	public AssaWriter(File file) {
		this.file = file;
	}

	public void save(String name, AssaStream<T> input) {
		AssaBuilder<T> builder = new AssaBuilder<>(name);
		while (input.hasNext()) {
			AssaStream.Item<T> next = input.next();
			builder.put(next.key(), next.object());
		}
		try {
			builder.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// TODO ver con JJ

//	private DataOutputStream output() throws FileNotFoundException {
//		return new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
//	}
//
//	private void writeItems(AssaStream input, DataOutputStream output) throws IOException {
//		while (input.hasNext()) {
//			AssaStream.Item item = input.next();
//			if (item == null) break;
//			output.writeLong(item.key());
//			output.writeInt(map(item.object()));
//		}
//	}
//
//	private void writeObjects(ObjectOutputStream output) {
//		objects().forEach(o -> writeObject(output, o));
//	}
//
//	private void writeObject(ObjectOutputStream output, Object object) {
//		try {
//			output.writeObject(object);
//		} catch (IOException e) {
//			Logger.error(e);
//		}
//	}
//
//	private int map(Object object) {
//		if (objects.containsKey(object)) return objects.get(object);
//		objects.put(object, objects.size());
//		return objects.size() - 1;
//	}
//
//	private Stream<Object> objects() {
//		return objects.entrySet()
//				.stream()
//				.sorted(comparing(Map.Entry::getValue))
//				.map(Map.Entry::getKey);
//	}
//
//	private ObjectOutputStream objectOutputStream(DataOutputStream output) throws IOException {
//		return new ObjectOutputStream(output);
//	}


}
