package io.intino.alexandria.assa.loaders;


import java.io.*;

public class FileAssa<T extends Serializable> extends InputStreamAssa<T> {
	private FileAssa(Class<T> type, DataInputStream input) throws IOException, ClassNotFoundException {
		super(type, input);
	}

	public static <X extends Serializable> InputStreamAssa<X> of(File file, Class<X> type) throws IOException {
		return of(new BufferedInputStream(new FileInputStream(file)), type);
	}


}
