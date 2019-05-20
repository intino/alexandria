package io.intino.konos.builder.codegeneration.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.util.stream.Collectors.toMap;
import static org.slf4j.Logger.ROOT_LOGGER_NAME;

public class CacheWriter extends HashMap<String, Integer> {
	private final File folder;

	private static Logger logger = LoggerFactory.getLogger(ROOT_LOGGER_NAME);

	public CacheWriter(File folder) {
		this.folder = folder;
	}

	public void save(ElementCache cache) {
		saveCacheFile(cache);
		saveAuditor(cache);
	}

	private void saveCacheFile(ElementCache cache) {
		try {
			Map<String, String> map = cache.stream().collect(toMap(String::valueOf, e -> ""));
			Properties properties = new Properties();
			properties.putAll(map);
			properties.store(new FileOutputStream(folder + "/.cache"), "");
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void saveAuditor(ElementCache cache) {
		cache.auditor.commit();
	}
}
