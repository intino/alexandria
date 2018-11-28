package io.intino.alexandria.ui.utils;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StreamUtil {
	public static void close(InputStream stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (Exception var2) {
				var2.printStackTrace();
			}
		}
	}


	public static void close(OutputStream stream) {
		if (stream != null) {
			try {
				stream.flush();
				stream.close();
			} catch (Exception var2) {
				;
			}
		}

	}

	public static void close(Reader reader) {
		if (reader != null) {
			try {
				reader.close();
			} catch (Exception var2) {
				;
			}
		}

	}

	public static void close(Writer writer) {
		if (writer != null) {
			try {
				writer.close();
			} catch (Exception var2) {
				;
			}
		}

	}

	public static final int copyData(InputStream input, OutputStream output) throws IOException {
		int size = 0;

		int len;
		for (byte[] buff = new byte[16384]; (len = input.read(buff)) > 0; size += len) {
			output.write(buff, 0, len);
		}

		return size;
	}

	public static final byte[] readBytes(InputStream input) throws IOException {
		if (input == null) {
			return new byte[0];
		} else {
			byte[] buffer = new byte[4096];
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			int nBytes;
			while ((nBytes = input.read(buffer)) != -1) {
				baos.write(buffer, 0, nBytes);
			}

			return baos.toByteArray();
		}
	}

	public static byte[] calculateHash(InputStream inputStream) throws IOException, NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA1");
		byte[] buffer = new byte[4096];
		boolean var3 = false;

		int readed;
		while ((readed = inputStream.read(buffer, 0, buffer.length)) > 0) {
			digest.update(buffer, 0, readed);
		}

		return digest.digest();
	}
}
