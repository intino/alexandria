package io.intino.konos.datalake;

import io.intino.ness.datalake.Scale;

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

	static io.intino.sezzet.model.graph.rules.Scale setScaleOf(String url) {
		return url.contains("?") ? io.intino.sezzet.model.graph.rules.Scale.valueOf(url.split("=")[1]) : io.intino.sezzet.model.graph.rules.Scale.Day;
	}
}
