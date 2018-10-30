package io.intino.alexandria.nessaccesor;

import io.intino.alexandria.ness.Scale;

import java.io.File;
import java.io.IOException;

public class Helper {
	public static File eventDatalakeDirectory(String url) {
		try {
			return new File(clean(url), "datalake").getCanonicalFile();
		} catch (IOException e) {
			return new File(clean(url), "datalake");
		}
	}

	static File setDatalakeDirectory(String url) {
		try {
			return new File(clean(url), "set").getCanonicalFile();
		} catch (IOException e) {
			return new File(clean(url), "set");
		}
	}

	static String clean(String url) {
		final int index = url.indexOf("?");
		if (index != -1) url = url.substring(0, index);
		return url.replace("file://", "");
	}

	public static Scale scaleOf(String url) {
		return url.contains("?") ? Scale.valueOf(url.split("=")[1]) : Scale.Day;
	}

	static io.intino.alexandria.ness.Scale setScaleOf(String url) {
		return url.contains("?") ? io.intino.alexandria.ness.Scale.valueOf(url.split("=")[1]) : io.intino.alexandria.ness.Scale.Day;
	}
}
