package io.intino.alexandria.zit;

import com.github.luben.zstd.ZstdInputStream;
import com.github.luben.zstd.ZstdOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Zit {

	public static final String ZIT_EXTENSION = ".zit";
	public static final String DELIMITER = "\0";


	public static InputStream decompressing(InputStream in) throws IOException {
		return in instanceof ZstdInputStream ? in : new ZstdInputStream(in);
	}

	public static OutputStream compressing(OutputStream out) throws IOException {
		return out instanceof ZstdOutputStream ? out : new ZstdOutputStream(out);
	}
}
