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
	private final File cacheFile;

	private static Logger logger = LoggerFactory.getLogger(ROOT_LOGGER_NAME);

	public CacheWriter(File cacheFile) {
		this.cacheFile = cacheFile;
	}

	public void save(Cache dictionary) {
		try {
			Properties properties = new Properties();
			properties.putAll(dictionary);
			properties.store(new FileOutputStream(cacheFile), "");
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}
}
