package io.intino.alexandria.zim;

import com.github.luben.zstd.ZstdInputStream;
import com.github.luben.zstd.ZstdOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class Zim {

	public static InputStream decompressing(InputStream in) throws IOException {
		return new ZstdInputStream(in);
	}

	public static OutputStream compressing(OutputStream out) throws IOException {
		return new ZstdOutputStream(out);
	}
}
