package io.intino.konos.builder.codegeneration.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import static org.slf4j.Logger.ROOT_LOGGER_NAME;

public class CacheWriter extends HashMap<String, Integer> {
	private final File folder;

	private static Logger logger = LoggerFactory.getLogger(ROOT_LOGGER_NAME);

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
			logger.error(e.getMessage(), e);
		}
	}

	private void saveAuditor(LayerCache cache) {
		cache.auditor.commit();
	}
}
