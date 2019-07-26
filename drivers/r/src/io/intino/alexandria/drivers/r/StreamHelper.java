package io.intino.alexandria.drivers.r;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamHelper {

	public static void copy(InputStream clientStream, OutputStream serverStream) throws IOException {
		byte [] buffer = new byte[8192];

		int c = clientStream.read(buffer);
		while(c >= 0) {
			serverStream.write(buffer,0, c);
			c = clientStream.read(buffer);
		}

		serverStream.close();
		clientStream.close();
	}

}
