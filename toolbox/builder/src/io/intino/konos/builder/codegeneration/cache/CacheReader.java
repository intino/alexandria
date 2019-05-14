package io.intino.konos.builder.codegeneration.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.util.stream.Collectors.toMap;
import static org.slf4j.Logger.ROOT_LOGGER_NAME;

public class CacheReader extends HashMap<String, Integer> {
	private final File cacheFile;

	private static Logger logger = LoggerFactory.getLogger(ROOT_LOGGER_NAME);

	public CacheReader(File cacheFile) {
		this.cacheFile = cacheFile;
	}

	public ElementCache load() {
		try {
			if (!cacheFile.exists()) return new ElementCache();
			Properties properties = new Properties();
			properties.load(new FileInputStream(cacheFile));
			Map<String, Long> marks = properties.entrySet().stream().collect(toMap(e -> String.valueOf(e.getKey()), e -> Long.valueOf(String.valueOf(e.getValue()))));
			return new ElementCache(marks);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
}
