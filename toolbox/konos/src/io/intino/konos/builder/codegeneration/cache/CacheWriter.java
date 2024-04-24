package io.intino.konos.builder.codegeneration.cache;

import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;


public class CacheWriter extends HashMap<String, Integer> {
	private final File folder;


	public CacheWriter(File folder) {
		this.folder = folder;
	}

	public void save(LayerCache cache) {
		saveCacheFile(cache);
		saveAuditor(cache);
	}

	private void saveCacheFile(LayerCache cache) {
		try {
			Properties properties = new Properties();
			properties.putAll(cache);
			properties.store(new FileOutputStream(folder + "/.cache"), "");
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private void saveAuditor(LayerCache cache) {
		cache.auditor.commit();
	}
}
