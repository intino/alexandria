package io.intino.konos.builder.codegeneration.cache;

import io.intino.konos.model.graph.KonosGraph;
import io.intino.tara.magritte.stores.FileSystemStore;
import io.intino.tara.magritte.utils.StoreAuditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import static java.util.stream.Collectors.toList;
import static org.slf4j.Logger.ROOT_LOGGER_NAME;

public class CacheReader extends HashMap<String, Integer> {
	private final File folder;

	private static Logger logger = LoggerFactory.getLogger(ROOT_LOGGER_NAME);

	public CacheReader(File folder) {
		this.folder = folder;
	}

	public ElementCache load(KonosGraph graph) {
		StoreAuditor auditor = loadAuditor(graph);
		return loadCacheFile(auditor);
	}

	private StoreAuditor loadAuditor(KonosGraph graph) {
		StoreAuditor auditor = new StoreAuditor(new FileSystemStore(folder));
		Arrays.stream(graph.core$().openedStashes()).forEach(auditor::trace);
		return auditor;
	}

	private ElementCache loadCacheFile(StoreAuditor auditor) {
		try {
			File cacheFile = new File(folder + "/.cache");
			if (!cacheFile.exists()) return new ElementCache(auditor);
			Properties properties = new Properties();
			properties.load(new FileInputStream(cacheFile));
			List<String> marks = properties.entrySet().stream().map(e -> (String)e.getKey()).collect(toList());
			return new ElementCache(auditor, marks);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
}
